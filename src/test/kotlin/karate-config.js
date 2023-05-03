function fn() {
  // Configure timeout
  karate.configure('connectTimeout', 120000);
  karate.configure('readTimeout', 120000);
  karate.configure('logPrettyResponse', true);

  var contextPath = '/graphql';

  var config = {
    local: {
      url: 'http://localhost:8080',
    },
    demo: {
      url: 'http://entrypoint-pve.demo.ecom-internal.gl.rocks' + contextPath,
    },
    pp: {
      url: 'http://entrypoint-pve.pp.ecom-internal.gl.rocks' + contextPath,
    },
    pr: {
      url: 'http://entrypoint-pve.pr.ecom-internal.gl.rocks' + contextPath,
    }
  };

  var env = karate.env ? karate.env : 'pp';

  karate.log('karate.env system property is:', karate.env);
  karate.log('env property is set to', env);

  return {
    config: config[env],
    env: env,
    baseUrl: config[env].url
  };
}
