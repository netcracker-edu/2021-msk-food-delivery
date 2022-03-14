import Config from "./Config";
import { commonFetch } from "../helpers/fetchers.js";

export default class  OrderClient {
  auth;
  constructor(auth) {
    this.config = new Config();
    this.auth = auth;
  }

  async calculateTotalPrice(cartItems) {
    if (this.config.tokenExpired()) {
      await this.auth.refreshToken();
    }
    const geo = {
      "lat" : 55.809327,
      "lon" : 37.632502,
    };
    const warehouseId = 4;
    return commonFetch(this.config.ORDER_URL+"/price",
                    "POST",
                    this.config.headersWithAuthorization(),
                    JSON.stringify(
                        {
                          "geo" : geo,
                          "warehouseId" : warehouseId,
                          "products" : cartItems
                        }));
  }
}
