
var keypress = require('keypress');

var psTree = require('ps-tree');

var kill = function (pid, signal, callback) {
    signal   = signal || 'SIGKILL';
    callback = callback || function () {};
    var killTree = true;
    if(killTree) {
        psTree(pid, function (err, children) {
            [pid].concat(
                children.map(function (p) {
                    return p.PID;
                })
            ).forEach(function (tpid) {
                try { process.kill(tpid, signal) }
                catch (ex) { }
            });
            callback();
        });
    } else {
        try { process.kill(pid, signal) }
        catch (ex) { }
        callback();
    }
};

var proc = require('child_process').exec('sox -d -d');

keypress(process.stdin);


process.stdin.on('keypress', function (ch, key)
{
	console.log('got "keypress"', key);

	if(key && key.name == 'k')
	{
		kill(proc.pid);
	}

	if(key && key.name == 's')
	{
		proc = require('child_process').exec('sox -d -d');
	}

	if(key && key.ctrl && key.name == 'c')
	{
		process.stdin.pause();
	}
});

process.stdin.setRawMode(true);
process.stdin.resume();



//var proc = require('child_process').exec('sox', ['-d', '-d']);

// var stdin = process.openStdin();
// process.stdin.setRawMode('true');

// stdin.on('keypress', function (chunk, key)
// {
// 	process.stdout.write('Get Chunk: ' + chunk + '\n');

// 	console.log('Pressed: ' + key.name);

// 	if (key && key.name == 'k')
// 	{
// 		//process.kill(proc.pid);
// 	}

// 	if (key && key.name == 's')
// 	{
// 		//proc = require('child_process').exec('sox -d -d');
// 	}

// 	if (key && key.ctrl && key.name == 'c')
// 	{
// 		process.exit();
// 	}
// })


