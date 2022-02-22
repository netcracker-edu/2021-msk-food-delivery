import Config from "./Config";
import { commonFetch } from "../helpers/fetchers.js";

export default class UserClient {
  constructor() {
    this.config = new Config();
  }

  async register(userInfo) {
    return commonFetch(
            this.config.SIGNUP_URL,
            "POST",
            this.config.defaultHeaders(),
            JSON.stringify(userInfo));
  }
}
