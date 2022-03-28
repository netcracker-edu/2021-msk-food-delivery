import Config from "./Config";
import { commonFetch } from "../helpers/fetchers.js";

export default class ProfileClient {
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
}