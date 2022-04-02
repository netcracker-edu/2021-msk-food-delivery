import Config from "./Config";
import { commonFetch } from "../helpers/fetchers.js";

export default class ProductClient {
  auth;
  constructor(auth) {
    this.config = new Config();
    this.auth = auth;
  }

  async fetchList(queryString, coords) {
    if (this.config.tokenExpired()) {
      await this.auth.refreshToken();
    }
    return commonFetch(this.config.PRODUCTS_URL+queryString,
                    "POST",
                    this.config.headersWithAuthorization(),
                    JSON.stringify(coords));
  }

  async count(coords) {
    if (this.config.tokenExpired()) {
      await this.auth.refreshToken();
    }
    return commonFetch(this.config.PRODUCTS_URL+"/count",
                    "POST",
                    this.config.headersWithAuthorization(),
                    JSON.stringify(coords));
  }

  async search(phrase, queryString, coords) {
    if (this.config.tokenExpired()) {
      await this.auth.refreshToken();
    }
    return commonFetch(this.config.PRODUCTS_URL+"/search"+queryString,
                    "POST",
                    this.config.headersWithAuthorization(),
                    JSON.stringify(
                        { "geo" : coords,
                          "phrase" : phrase
                        }));
  }

  async searchCount(phrase, coords) {
    if (this.config.tokenExpired()) {
      await this.auth.refreshToken();
    }
    return commonFetch(this.config.PRODUCTS_URL+"/search/count",
                    "POST",
                    this.config.headersWithAuthorization(),
                    JSON.stringify(
                        { "geo" : coords,
                          "phrase" : phrase
                        }));
  }

  async fetchCartProducts(cartItems) {
    if (this.config.tokenExpired()) {
      await this.auth.refreshToken();
    }
    return commonFetch(this.config.PRODUCTS_URL+"/cart",
                    "POST",
                    this.config.headersWithAuthorization(),
                    JSON.stringify(cartItems));
  }
}
