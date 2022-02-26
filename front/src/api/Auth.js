import Config from "./Config";
import { tokenFetch, clearTokenFetch } from "../helpers/fetchers.js";

class Auth {
  token;
  setToken;

  constructor(argToken, argSetToken) {
    this.token = argToken;
    this.setToken = argSetToken;
    this.config = new Config();
  }

  async loginUser(credentials) {
    return tokenFetch(
            this.config.SIGNIN_URL,
            this.config.defaultHeaders(),
            JSON.stringify(credentials),
            this.clearTokens.bind(this),
            this.storeTokens.bind(this));
  }

  async refreshToken() {
    return tokenFetch(
          this.config.REFRESH_TOKEN_URL,
          this.config.headersWithAuthorization(),
          JSON.stringify({ refreshToken: this.token.refreshToken }),
          this.clearTokens.bind(this),
          this.storeTokens.bind(this));
  }

  async logoutUser(refreshToken) {
     return clearTokenFetch(
              this.config.SIGNOUT_URL,
              this.config.defaultHeaders(),
              JSON.stringify(refreshToken),
              this.clearTokens.bind(this));
   }

  storeTokens(json) {
    this.setToken(json);
    this.config.storeAccessToken(json.accessToken);
  }

  clearTokens() {
    this.config.storeAccessToken("");
    this.setToken(null);
  }
}

export default Auth;
