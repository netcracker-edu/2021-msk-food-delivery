import {  Button, InputNumber,Typography} from "antd";
import { DeleteTwoTone } from '@ant-design/icons';

const {Link} = Typography;

const ItemCountInput = ({count, addToCart, deleteFromCart}) => {
  return(
    <>
      {count == 0
        ? <Button onClick={() => addToCart(1)}> В корзину </Button>
        : <InputNumber
                  addonAfter={
                    <Link onClick={deleteFromCart}>
                      <DeleteTwoTone style={{ fontSize: '20px' }} twoToneColor="#eb2f96" />
                    </Link>
                  }
                  value={count} min={0} max={100}
                  onChange={(value) => { value == 0
                                          ? deleteFromCart()
                                          : addToCart(value)}}/>

      }
    </>
  );
}

export default ItemCountInput;
