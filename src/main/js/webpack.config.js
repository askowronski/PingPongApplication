var path = require('path');

module.exports = {
    entry: './src/index.js',
    cache: true,
    debug: true,
    output: {
        path: __dirname,
        filename: './src/main/resources/templates'
    },
    module: {
        loaders: [
            {
                test: path.join(__dirname, '.'),
                exclude: /(node_modules)/,
                loader: 'babel',
                query: {
                    cacheDirectory: true,
                    presets: ['es2015, react']
                }
            }
        ]
    },
    plugins: [
        new UglifyJsPlugin({
            parallel: true,
            uglifyOptions: {
                ie8: false,
                warnings: true,
                output: {
                    comments: false,
                    beautify: false,
                }
            }
        })
    ]
};