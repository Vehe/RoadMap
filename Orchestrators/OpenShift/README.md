# OpenShift

![OpenShift logo](https://www.openshift.com/hubfs/images/logos/osh/Logotype_RH_OpenShiftContainerPlatform_wLogo_RGB_Gray.svg)

## Architecture

[DOC](https://docs.openshift.com/container-platform/3.9/architecture/index.html)

The Docker service provides the abstraction for packaging and creating Linux-based, lightweight container images. Kubernetes provides the cluster management and orchestrates containers on multiple hosts.

- OpenShift Container Platform adds:
- Source code management, builds, and deployments for developers
- Managing and promoting images at scale as they flow through your system
- Application management at scale
- Team and user tracking for organizing a large developer organization
- Networking infrastructure that supports the cluster

OpenShift Container Platform has a microservices-based architecture of smaller, decoupled units that work together. It runs on top of a Kubernetes cluster, with data about the objects stored in etcd, a reliable clustered key-value store. Those services are broken down by function:

- REST APIs, which expose each of the core objects.
- Controllers, which read those APIs, apply changes to other objects, and report status or write back to the object.

Users make calls to the REST API to change the state of the system. Controllers use the REST API to read the user’s desired state, and then try to bring the other parts of the system into sync. For example, when a user requests a build they create a "build" object. The build controller sees that a new build has been created, and runs a process on the cluster to perform that build. When the build completes, the controller updates the build object via the REST API and the user sees that their build is complete.

## Change Log In Identity Provider

[DOC](https://docs.openshift.com/container-platform/3.9/getting_started/configure_openshift.html#change-log-in-identity-provider)

The default behavior of a freshly installed OpenShift Container Platform instance is to deny any user from logging in. To change the authentication method to HTPasswd:

- Open the `/etc/origin/master/master-config.yaml` file in edit mode.
- Find the identityProviders section.
- Change `DenyAllPasswordIdentityProvider` to `HTPasswdPasswordIdentityProvider` provider.
- Change the value of the name label to `htpasswd_auth` and add a new line file: `/etc/origin/openshift-passwd` in the provider section.

An example identityProviders section with `HTPasswdPasswordIdentityProvider` would look like the following.
```
oauthConfig:
  ...
  identityProviders:
  - challenge: true
    login: true
    name: htpasswd_auth provider
    provider:
      apiVersion: v1
      kind: HTPasswdPasswordIdentityProvider
      file: /etc/origin/openshift-passwd
```

## User Accounts

[DOC](https://docs.openshift.com/container-platform/3.9/getting_started/configure_openshift.html#create-user-accounts)

By default, when installed for the first time, there are no roles or user accounts created in OpenShift Container Platform, so you need to create them. You have the option to either create new roles or define a policy that allows anyone to log in (to start you off).

Before you do anything else, log in at least one time with the default system:admin user. On the master, run the following command:
```sh
$ oc login -u system:admin
```

By logging in at least one time with this account, you will create the system:admin user’s configuration file, which will allow you to log in subsequently.

There is no password for this system account.

### Create User

You can use the httpd-tools package to obtain the htpasswd binary that can generate these accounts.
```sh
# yum -y install httpd-tools
```

Create a user account.
```sh
# touch /etc/origin/openshift-passwd
# htpasswd -b /etc/origin/openshift-passwd admin redhat
```

You have created a user, admin, with the password, redhat.
Restart OpenShift before going forward.
```sh
# systemctl restart atomic-openshift-master-api atomic-openshift-master-controllers
```

Give this user account cluster-admin privileges, which allows it to do everything.
```sh
$ oc adm policy add-cluster-role-to-user cluster-admin admin
```

When running oc adm commands, you should run them only from the first master listed in the Ansible host inventory file, by default `/etc/ansible/hosts`.

You can use this username/password combination to log in via the web console or the command line. To test this, run the following command.
```sh
$ oc login -u admin
```

## Router 

[DOC](https://docs.openshift.com/container-platform/3.9/getting_started/configure_openshift.html#deploy-router)

The OpenShift router is the entry point for external network traffic destined for OpenShift services. It supports HTTP, HTTPS, and any TLS-enabled traffic that uses SIN (Server Name Indication - es una extensión del protocolo de seguridad TLS [+ Más Info](https://es.wikipedia.org/wiki/Server_Name_Indication)), which enables the router to send traffic to the correct service.

Without the router, OpenShift services and pods are unable to communicate with any resource outside of the OpenShift instance.

Delete the default router using the following command.
```sh
$ oc delete all -l router=router
```

Create a new default router.
```sh
$ oc adm router --replicas=1 --service-account=router
```