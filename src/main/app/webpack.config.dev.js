const {merge} = require('webpack-merge');
const {EnvironmentPlugin} = require('webpack');
const baseConfig = require('./webpack.config');


module.exports = merge(baseConfig, {
    mode: 'development',
    devtool: 'source-map',
    devServer: {
        static: './dist',
        port: 3000,
    },
    plugins: [
        new EnvironmentPlugin({
            HOST_URL: 'http://localhost:8090'
        })
    ],
});
