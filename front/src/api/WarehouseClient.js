import Config from "./Config";
import { commonFetch } from "../helpers/fetchers";

export default class WarehouseClient {
  auth;
  constructor(auth) {
    this.auth = auth;
    this.config = new Config();
  }

  async checkToken(){
    if (this.config.tokenExpired()) {
        return this.auth.refreshToken();
    }
  }

  checkResponse(response){
    if(response.success){
        return response.data;
    }
    console.log(response.error);
    return null;
  }

  async getAllActive(){
      await this.checkToken();
      let response = await commonFetch(this.config.ACTIVE_WAREHOUSES_URL, 'GET', this.config.headersWithAuthorization(), null);
      return this.checkResponse(response);
    }

  async getNearestWarehouse(coords) {
    if (this.config.tokenExpired()) {
      await this.auth.refreshToken();
    }
    return commonFetch(
                this.config.WAREHOUSE_URL+`/nearest?lat=${coords.lat}&lon=${coords.lon}`,
                "GET",
                this.config.headersWithAuthorization(),
                null);
  }
}
