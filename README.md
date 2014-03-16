SimpleWebServer-mod
===================

SimpleWebServer, modified. Original version is at http://www.jibble.org/miniwebserver/

## License

The license of this software is the GNU General Public License (GPL) from the [Free Software Foundation](http://www.fsf.org/).

Original distribution's copyright is as follows:

    Copyright Paul James Mutton, 2001-2004, http://www.jibble.org/
    
    This file is part of Mini Wegb Server / SimpleWebServer.
    
    This software is dual-licensed, allowing you to choose between the GNU
    General Public License (GPL) and the www.jibble.org Commercial License.
    Since the GPL may be too restrictive for use in a proprietary application,
    a commercial license is also provided. Full license information can be
    found at http://www.jibble.org/licenses/



## Usage

* Added two options to the original
    * `-root <rootPath>` [NOT REQUIRED]
        * Set server's root (default: current directory)
    * `-port <port>` [NOT REQUIRED]
        * Set server's listen port (default: 80)
* Running
    * `java -jar SimpleWebServer-mod-0.0.1.jar -root /tmp -port 8080`
* Building
    * `mvn clean package`
