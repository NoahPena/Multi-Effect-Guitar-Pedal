
var fork = require('child_process').fork;

var child;

function killCommand()
{
	process.exit();
}

child = fork('./Reverb.js');

setTimeout(killCommand, 3000);