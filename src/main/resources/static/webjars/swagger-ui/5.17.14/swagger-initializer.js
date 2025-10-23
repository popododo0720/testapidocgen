window.onload = function() {
  window.ui = SwaggerUIBundle({
    url: "/openapi3/openapi3.json",
    dom_id: '#swagger-ui',
    deepLinking: true,
    queryConfigEnabled: true,
    presets: [
      SwaggerUIBundle.presets.apis,
      SwaggerUIStandalonePreset
    ],
    plugins: [
      SwaggerUIBundle.plugins.DownloadUrl
    ],
    layout: "StandaloneLayout"
  });
};
