
var spawn = require('child_process').spawn;

console.log('Now playing Reverb');

var a = spawn('sox', ['-d', '-d', 'reverb', '80']);


function anotherOne()
{
	process.kill(a.pid);

	console.log('Now Playing Chorus');

	//chorus 0.7 0.9 55 0.4 0.25 2 −t
	var b = spawn('sox', ['-d', '-d', 'chorus', '0.7', '0.9', '55', '0.4', '0.25', '2', '-t']);
}


setTimeout(anotherOne, 3000);