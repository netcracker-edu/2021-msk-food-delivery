import { useEffect, useState } from "react";
import ProductClient from "../../api/ProductClient";
import Products from "./Products";
import { Layout, Pagination, Input, Divider } from "antd";
const { Content } = Layout;
const { Search } = Input;

const ProductList = ({auth, address, setAddress}) => {

  const [productList, setProductList] = useState();
  const [noRecMsg, setNoRecMsg] = useState("Loading...");
  const [searchPhrase, setSearchPhrase] = useState("");
  const [total, setTotal] = useState();
  const productClient = new ProductClient(auth);
  const coords = { "lat" : address.coord[0], "lon" : address.coord[1]};

  async function fetchProducts(page, pageSize) {
    const responseCount = await productClient.count(coords);
    if (responseCount && responseCount.success) {
      setTotal(responseCount.data.count);
      const queryString = `?page=${page-1}&size=${pageSize}`;
      const response = await productClient.fetchList(queryString, coords);
      if (response && response.success) {
        setProductList(response.data);
      } else {
        setNoRecMsg(response);
      }
    } else {
      setNoRecMsg(responseCount);
    }
    setSearchPhrase("");
  }

  const onSearch = async (phrase, page, pageSize) => {
    const responseCount = await productClient.searchCount(phrase, coords);
    if (responseCount && responseCount.success) {
      setTotal(responseCount.data.count);
      const queryString = page != null ? `?page=${page-1}&size=${pageSize}` : "";
      const response = await productClient.search(phrase, queryString, coords);
      if (response && response.success) {
        setProductList(response.data);
      } else {
        setNoRecMsg(response);
      }
    } else {
      setNoRecMsg(responseCount);
    }
    setSearchPhrase(phrase);
  }

  useEffect(() => {
    fetchProducts(1, 12);
  }, []);

  return (
    <Content className="wrapper">
      {productList ? (
        <>
          <br></br>
          <Search
            placeholder="input search text"
            allowClear
            enterButton="Search"
            size="large"
            onSearch={(value) => value == null || value == ""
                            ? fetchProducts(1, 12)
                            : onSearch(value, 1, 12)}
          />
          <Divider></Divider>
          <Products auth={auth} productList={productList ? productList : []} />
          <Divider></Divider>
          <Pagination total={total == null ? 0 : total}
                      defaultPageSize={12}
                      onChange={(page, pageSize) =>
                            searchPhrase == null || searchPhrase == ""
                              ? fetchProducts(page, pageSize)
                              : onSearch(searchPhrase, page, pageSize)}/>
        </>
      ) : (
        <h3>{noRecMsg}</h3>
      )}
    </Content>
  );
}

export default ProductList;
