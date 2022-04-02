import { useState } from "react";

export default function useAddress() {
  const getAddress = () => {
    const address = localStorage.getItem("address");
    const addressInfo = address ? JSON.parse(address) : {};
    return addressInfo;
  }

  const [address, setAddress] = useState(getAddress());

  const saveAddress = (address) => {
    localStorage.setItem("address", JSON.stringify(address));
    setAddress(address);
  };

  return {
    setAddress: saveAddress,
    address
  };
}
