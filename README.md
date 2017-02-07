# PicoBramble
A lightweight library for running jobs across multiple machines. Originally designed for a cluster of Raspberry Pis.

Built as a learning exercise - you should probably use something like the [JPPF](http://www.jppf.org/) framework for serious projects. This project is _probably not_ secure. I recommend running this on a private, wired network which is not connected to the internet. 

This project is built using [Maven](https://maven.apache.org/). It depends on the [FST serialization library](https://github.com/RuedigerMoeller/fast-serialization) and [jetty](https://eclipse.org/jetty/).

To use the raspberry pi diagnostics, you will need to run the following commands in your raspberry pi's shell:

`sudo chmod 444 /sys/class/thermal/thermal_zone0/temp`

`sudo chmod 444 /sys/devices/system/cpu/cpufreq/policy0/scaling_cur_freq`

These commands make the system files readable by Java, which will report the temperatures and cpu speeds back to the master node.

The web GUI will need to be served to localhost on the same computer as the master node is running on. I've included `simple_web_server.py` (which uses Python 3), but any server that can serve a collection of static files is suitable.
