import Config from "./Config";

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
    return fetch(this.config.PROFILE_URL, {
      method: "GET",
      mode: "cors",
      headers: {
        ...this.config.headersWithAuthorization(),
      },
    })
      .then((response) => Promise.all([response, response.json()]))
      .then(([response, json]) => {
        console.log("Response JSON: " + JSON.stringify(json));
        if (!response.ok) {
          return { success: false, error: json };
        }
        return { success: true, data: json };
      })
      .catch((e) => {
        this.handleError(e);
      });
  }

  async editCommonInfo(info) {
    if (this.config.tokenExpired()) {
      await this.auth.refreshToken();
    }
    return fetch(this.config.PROFILE_URL, {
      method: "PUT",
      mode: "cors",
      headers: {
        ...this.config.headersWithAuthorization(),
      },
      body: JSON.stringify(info),
    })
      .then((response) => Promise.all([response, response.json()]))
      .then(([response, json]) => {
        console.log("Response JSON: " + JSON.stringify(json));
        if (!response.ok) {
          return { success: false, error: json };
        }
        return { success: true, data: json };
      })
      .catch((e) => {
        this.handleError(e);
      });
  }

  async editEmail(info) {
    if (this.config.tokenExpired()) {
      await this.auth.refreshToken();
    }
    return fetch(this.config.PROFILE_URL+"/email", {
      method: "PATCH",
      mode: "cors",
      headers: {
        ...this.config.headersWithAuthorization(),
      },
      body: JSON.stringify(info),
    })
      .then((response) => Promise.all([response, response.json()]))
      .then(([response, json]) => {
        console.log("Response JSON: " + JSON.stringify(json));
        if (!response.ok) {
          return { success: false, error: json };
        }
        return this.auth.refreshToken()
          .then((res) => {
             return { success: true, data: json };
          });
      })
      .catch((e) => {
        this.handleError(e);
      });
  }

  async editPassword(values) {
    if (this.config.tokenExpired()) {
      await this.auth.refreshToken();
    }
    return fetch(this.config.PROFILE_URL+"/password", {
      method: "PATCH",
      mode: "cors",
      headers: {
        ...this.config.headersWithAuthorization(),
      },
      body: JSON.stringify(values),
    })
      .then((response) => Promise.all([response, response.json()]))
      .then(([response, json]) => {
        console.log("Response JSON: " + JSON.stringify(json));
        if (!response.ok) {
          return { success: false, error: json };
        }
          return { success: true, data: json };
      })
      .catch((e) => {
        this.handleError(e);
      });
  }

  handleError(error) {
  const err = new Map([
    [TypeError, "There was a problem fetching the response."],
    [SyntaxError, "There was a problem parsing the response."],
    [Error, error.message],
  ]).get(error.constructor);
  console.log(err);
  return err;
}
}
