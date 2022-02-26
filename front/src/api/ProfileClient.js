import Config from "./Config";
import { commonFetch } from "../helpers/fetchers.js";

export default class ProfileClient {
  auth;
  constructor(auth) {
    this.auth = auth;
    this.config = new Config();
  }

  async get() {
    if (this.config.tokenExpired()) {
      await this.auth.refreshToken();
    }
    return commonFetch(
            this.config.PROFILE_URL,
            "GET",
            this.config.headersWithAuthorization(),
            null);
  }

  async editCommonInfo(info) {
    if (this.config.tokenExpired()) {
      await this.auth.refreshToken();
    }
    return commonFetch(
            this.config.PROFILE_URL,
            "PUT",
            this.config.headersWithAuthorization(),
            JSON.stringify(info));
  }

  async editEmail(info) {
    if (this.config.tokenExpired()) {
      await this.auth.refreshToken();
    }
    return commonFetch(
            this.config.PROFILE_URL+"/email",
            "PATCH",
            this.config.headersWithAuthorization(),
            JSON.stringify(info));
  }

  async editPassword(values) {
     if (this.config.tokenExpired()) {
       await this.auth.refreshToken();
     }
     return commonFetch(
              this.config.PROFILE_URL+"/password",
              "PATCH",
              this.config.headersWithAuthorization(),
              JSON.stringify(values));
   }
}
