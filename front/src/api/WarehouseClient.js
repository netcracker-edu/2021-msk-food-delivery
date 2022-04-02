import Config from "./Config";
import { commonFetch } from "../helpers/fetchers";

export default class WarehouseClient {
  auth;
  constructor(auth) {
    this.auth = auth;
    this.config = new Config();
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
