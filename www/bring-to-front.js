var exec = require('cordova/exec');

// module.exports = function() {
//   cordova.exec(null, null, "bringtofront", "bringToFront", [])
// }

exports.test = function() {
    exec(null, null, "bringtofront", "test", []);
};

exports.bringtofront = function() {
   cordova.exec(null, null, "bringtofront", "bringToFront", [])
};
