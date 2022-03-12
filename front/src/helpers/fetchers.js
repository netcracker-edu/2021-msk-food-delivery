exports.commonFetch = async (url, method, headers, body) => {
  return fetch(url, {
    method: method,
    mode: "cors",
    headers: { ...headers, },
    body: body,
  })
    .then((response) => Promise.all([response, response.json()]))
    .then(([response, json]) => {
      console.log("Response JSON: " + JSON.stringify(json));
      if (!response.ok) {
        return { success: false, error: json };
      }
        return { success: true, data: json };
    })
    .catch((e) => handleError(e));
};

exports.patchFetch = async (url, headers, body) => {
  return fetch(url, {
    method: 'PATCH',
    mode: "cors",
    headers: { ...headers, },
    body: body,
  })
};

exports.tokenFetch = async (url, headers, body, clear, store) => {
  return fetch(url, {
    method: "POST",
    mode: "cors",
    headers: {
      ...headers,
    },
    body: body,
  })
    .then((response) => Promise.all([response, response.json()]))
    .then(([response, json]) => {
      if (!response.ok) {
        clear();
        return { success: false, error: json };
      }
      store(json);
      return { success: true, data: json };
    })
    .catch((e) => handleError(e));
};

exports.clearTokenFetch = async (url, headers, body, clear) => {
  return fetch(url, {
    method: "POST",
    mode: "cors",
    headers: {
      ...headers,
    },
    body: body,
  })
    .then((response) => {
      clear();
      if (!response.ok) {
        const error = response.json();
        console.log(error);
        throw Error(error);
      }
      return response;
    })
    .catch((e) => handleError(e));
};

function handleError(error) {
  const err = new Map([
    [TypeError, "Can't connect to server."],
    [SyntaxError, "There was a problem parsing the response."],
    [Error, error.message],
  ]).get(error.constructor);
  console.log(err);
  return err;
};
