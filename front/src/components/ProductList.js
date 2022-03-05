import { useEffect, useState } from "react";
import ProductClient from "../api/ProductClient";
import Products from "./Products";
import { Layout, Pagination, Input } from "antd";
const {Content} = Layout;
const { Search } = Input;

const ProductList = ({auth}) => {

  const [productList, setProductList] = useState();
  const [noRecMsg, setNoRecMsg] = useState("Loading...");
  const [searchPhrase, setSearchPhrase] = useState("");
  const [total, setTotal] = useState();
  const productClient = new ProductClient(auth);

  async function fetchProducts(page, pageSize) {
    const responseCount = await productClient.count();
    if (responseCount && responseCount.success) {
      setTotal(responseCount.data.count);
      console.log(responseCount.data.count);
      const queryString = `?page=${page-1}&size=${pageSize}`;
      console.log(queryString);
      const response = await productClient.fetchList(queryString);
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
    const responseCount = await productClient.searchCount(phrase);
    if (responseCount && responseCount.success) {
      setTotal(responseCount.data.count);
      console.log(responseCount.data.count);
      const queryString = page != null ? `?page=${page-1}&size=${pageSize}` : "";
      const response = await productClient.search(phrase, queryString);
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
    fetchProducts(1, 10);
  }, []);

  return (
    <Content className="wrapper">
      {productList ? (
        <>
          <Search
            placeholder="input search text"
            allowClear
            enterButton="Search"
            size="large"
            onSearch={(value) => value == null || value == ""
                            ? fetchProducts(1, 10)
                            : onSearch(value, 1, 10)}
          />
          <Products auth={auth} productList={productList ? productList : []} />
          <Pagination total={total == null ? 0 : total}
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
