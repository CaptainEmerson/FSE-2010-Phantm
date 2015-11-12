### How to build a Ubuntu VM with JDK 7 installed:

  1.  Download and install [VirtualBox] (https://www.virtualbox.org/wiki/Downloads)
  2.  Download and install [Vagrant] (https://www.vagrantup.com/downloads.html)
  3.  Clone this Repository by using **git clone https://github.com/SoftwareEngineeringToolDemos/FSE-2010-Phantm.git**
  5.  Navigate to the build-vm folder of the cloned repository on your host machine
  6.  Run *vagrant up* to set up the vm. This would do following:
  
        a.Download and add the base box image in Vagrant. Adding the base box would remove the need for further downloads when the box is brought up at a later point of time.

        b. Create virtual machine using this image.
        
        c. Launch Ubuntu with GUI.
        
        d. Install JDK 7 using 'apt-get' command.
        
## References
[Vagrant shell documentation] (https://docs.vagrantup.com/v2/provisioning/shell.html)

## Acknowledgements
I would like to thank the uploader of the ubuntu desktop basebox "box-cutter/ubuntu1404-desktop" for making it availble on vagrant cloud.
