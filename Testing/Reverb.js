var SoxCommand = require('sox-audio');
var command = SoxCommand();

command.input('-d');
command.output('-d');
command.addEffect('reverb', 80);

command.on('prepare', function(args) 
{
  console.log('Preparing sox command with args ' + args.join(' '));
});
 
command.on('start', function(commandLine) 
{
  console.log('Spawned sox with command ' + commandLine);
});
 
command.on('progress', function(progress) 
{
  console.log('Processing progress: ', progress);
});
 
command.on('error', function(err, stdout, stderr) 
{
  console.log('Cannot process audio: ' + err.message);
  console.log('Sox Command Stdout: ', stdout);
  console.log('Sox Command Stderr: ', stderr)
});
 
command.on('end', function() 
{
  console.log('Ending Reverb...');
});

command.run();