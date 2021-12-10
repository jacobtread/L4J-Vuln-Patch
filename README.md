# WARNING
THIS EXPLOIT EFFECTS BOTH CLIENTS AND SERVERS

There is currently a exploit going around that affects all versions of Minecraft this exploit
abuses log4j deserialization in order to achieve remote code execution this exploit is not new but has
only just recently come to light because of paper mc and spigot etc patching and announcing this exploit

## Downloads
Currently the only available releases for this patcher are Jar releases but binaries will be available soon

## SERVER OWNERS
Please update your servers to the latest version to help protect against this most major
providers have already patched this vulnerability HOWERVER this is still unpatched on
all minecraft clients 

## CLIENTS
This tool is too help patch clients and protect them against the vuln because they are all vulnerable
all an attack has to do is put a payload in the game chat an all connected clients will be affected

## NOTE
THIS IS NOT A PERMANENT SOLUTION THIS ONLY IS A ONE TIME PATCH ON EACH CLIENT JAR UPDATING YOUR
MC VERSIONS OR INSTALLING NEW ONES WILL NOT BE AFFECTED BY THIS PATCH
AND YOU WILL NEED TO RUN IT AGAIN

## WHAT THIS PATCH DOES
This patch modifies each of the log file configurations in your mc jars replacing 

%msg with %msg{nolookups}

## RELEASES
Releases will be available in the releases tab