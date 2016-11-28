
var bleno = require('bleno');

var name = 'Kappa';

var uuid = ['0000110100001000800000805F9B34FB'];

bleno.state = 'poweredOn';

bleno.startAdvertising(name, uuid);