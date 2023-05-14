const {merge} = require('webpack-merge');
const {EnvironmentPlugin} = require('webpack');
const baseConfig = require('./webpack.config');


module.exports = merge(baseConfig, {
    mode: 'production',
    plugins: [
        new EnvironmentPlugin({
            HOST_URL: ''
        })
    ],
});
