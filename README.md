# st★rfish
![Linux](https://img.shields.io/badge/Linux-FCC624?style=for-the-badge&logo=linux&logoColor=black)<br>
![GitHub stars](https://img.shields.io/github/stars/sparksh1ne/stXrfish.svg)
![Releases](https://img.shields.io/github/v/release/sparksh1ne/stXrfish?label=release)

**st★rfish** (can be stylized as stXrfish or st*rfish) is a simple proxy parser that takes a list of free proxies from https://geonode.com/free-proxy-list and tries to connect to each IP address until it connects successfully.

## how to install
after downloading the release/pre-release, unzip it to a convenient location and inside the unzipped folder, run the `launch.sh` file with **superuser rights**.

## troubleshooting
* `java.nio.file.AccessDeniedException: /etc/environment` - run the `launch.sh` file with superuser rights.
* `java.net.SocketTimeoutException: Connect timed out` and similar - if the program output a bad proxy along with this exception, then this means that sending a verification request simply exceeded the waiting time, and did not return a response of 200, you can ignore this.
