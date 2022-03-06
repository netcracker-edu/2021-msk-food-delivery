import React, { createContext, useReducer, useContext } from "react";

// Context
export const CartContext = createContext();

// Custom hook to consume cart context
function useCartContext() {
  return useContext(CartContext);
}
// ----------------------------------------------------------------

// Actions
export const UPDATE_CART = "UPDATE_CART";
export const ADD_ITEM = "ADD_ITEM";
export const REMOVE_ITEM = "REMOVE_ITEM";

export function updateCart(items) {
  return { type: UPDATE_CART, items };
}
export function addItem(item) {
  return { type: ADD_ITEM, item };
}
export function removeItem(index) {
  return { type: REMOVE_ITEM, index };
}
// ----------------------------------------------------------------

// Reducer - update the state based on given action
export function cartReducer(state, action) {
  switch (action.type) {
    case UPDATE_CART:
      let prods = action?.items;
      localStorage.setItem("products", JSON.stringify(prods));
      return prods;
    case ADD_ITEM:
      let products = localStorage.getItem("products");
      products = products ? JSON.parse(products) : {};
      products[action.item.key] = action.item.value;
      localStorage.setItem("products", JSON.stringify(products));
      return products;
    case REMOVE_ITEM:
      let products2 = localStorage.getItem("products");
      products2 = products2 ? JSON.parse(products2) : {};
      delete products2[action.index];
      localStorage.setItem("products", JSON.stringify(products2));
      return products2;
    default:
      return state;
  }
}

// ----------------------------------------------------------------

const CartContextProvider = (props) => {
  const [cartItems, dispatch] = useReducer(cartReducer, []);
  const cartData = { cartItems, dispatch };
  return <CartContext.Provider value={cartData} {...props} />;
};

export { CartContextProvider, useCartContext };
