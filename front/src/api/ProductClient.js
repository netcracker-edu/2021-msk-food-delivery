import Config from "./Config";
import { commonFetch } from "../helpers/fetchers.js";

export default class ProductClient {
  auth;
  constructor(auth) {
    this.config = new Config();
    this.auth = auth;
  }

  async fetchList(queryString) {
    if (this.config.tokenExpired()) {
      await this.auth.refreshToken();
    }
    return commonFetch(this.config.PRODUCTS_URL+queryString,
                    "POST",
                    this.config.headersWithAuthorization(),
                    JSON.stringify(
                        {
                          "lat" : 55.809327,
                          "lon" : 37.632502,
                        }));
  }

  async count() {
    if (this.config.tokenExpired()) {
      await this.auth.refreshToken();
    }
    return commonFetch(this.config.PRODUCTS_URL+"/count",
                    "POST",
                    this.config.headersWithAuthorization(),
                    JSON.stringify(
                        {
                          "lat" : 55.809327,
                          "lon" : 37.632502,
                        }));
  }

  async search(phrase, queryString) {
    if (this.config.tokenExpired()) {
      await this.auth.refreshToken();
    }
    return commonFetch(this.config.PRODUCTS_URL+"/search"+queryString,
                    "POST",
                    this.config.headersWithAuthorization(),
                    JSON.stringify(
                        { "geo" : {
                            "lat" : 55.809327,
                            "lon" : 37.632502,
                          },
                          "phrase" : phrase
                        }));
  }

  async searchCount(phrase) {
    if (this.config.tokenExpired()) {
      await this.auth.refreshToken();
    }
    return commonFetch(this.config.PRODUCTS_URL+"/search/count",
                    "POST",
                    this.config.headersWithAuthorization(),
                    JSON.stringify(
                        { "geo" : {
                            "lat" : 55.809327,
                            "lon" : 37.632502,
                          },
                          "phrase" : phrase
                        }));
  }
}
