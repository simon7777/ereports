const path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');


module.exports = {
    output: {
        path: path.resolve(__dirname, 'dist'),
        filename: 'bundle.[contenthash].js',
        clean: true,
    },
    devServer: {
        open: true,
        hot: true,
        compress: true,
        historyApiFallback: true,
        liveReload: true,
    },
    resolve: {
        extensions: ['.js', '.jsx'],
        alias: {
            '~': path.resolve(__dirname, 'src'),
        },
    },
    module: {
        rules: [
            {
                test: /\.(sass|less|css|scss)$/,
                use: [
                    'style-loader',
                    'css-loader'
                ],
            },
            {
                test: /\.(js|jsx|ts|tsx)$/,
                exclude: /node_modules/,
                use: {
                    loader: 'babel-loader',
                    options: {
                        presets: ['@babel/env', '@babel/preset-react'],
                    },
                },
            },
            {
                test: /\.(png|svg|jpg|jpeg|gif|ico)$/i,
                type: 'asset/resource',
            },
            {
                test: /\.(woff|woff2|eot|ttf|otf)$/i,
                type: 'asset/resource',
            },
        ],
    },
    plugins: [
        new HtmlWebpackPlugin({
            title: 'eReports',
            filename: 'index.html',
            template: 'public/index.html'
        }),
    ],
    mode: 'development',
    stats: {
        errorDetails: true,
    },
};
