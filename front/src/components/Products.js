import ProductCard from "./ProductCard";
import { Row } from "antd";


const Products = ({ auth, productList}) => {
  return (
    <Row justify="center" gutter= {[16, 16]}>
      {productList.map((item) => (
        <ProductCard key={item.id} product={item} auth={auth} />
      ))}
    </Row>
  );
};

export default Products;
