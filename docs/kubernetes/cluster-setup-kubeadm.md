# Kubernetes Cluster Setup using `kubeadm`

_using [kubeadm](https://kubernetes.io/docs/setup/production-environment/tools/kubeadm/install-kubeadm/)_

__Duration: ~10 hours without success__

## On Hyper-V Server:
- Creating 3 hosts with 4 vCores, 8 GB RAM, 32 GB SSDs, using Debian 12.9
    - Under Hyper-V, make sure to deactivate Secure Boot to actually being able to load the ISO image for installation
    - k8s-node-1.k8s.jochenlinnemann.de
    - k8s-node-2.k8s.jochenlinnemann.de
    - k8s-node-3.k8s.jochenlinnemann.de

## On Server VMs (Hyper-V Console):
- Verifying MAC address and product_uuid uniqueness among nodes
    - ...135
    - ...136
    - ...138
- Updating system components (if necessary)
    - `apt install sudo`
    - `/sbin/usermod -aG sudo <user>`
    - `sudo apt install openssh-server`
    - `sudo systemctl enable ssh`
    - `sudo apt install ufw`
    - `sudo ufw allow ssh`

## On Server VMs (SSH Console):
- Disabling swap: `sudo swapoff -a`
- Opening the firewall for [required ports](https://kubernetes.io/docs/reference/networking/ports-and-protocols/):
    - `sudo ufw allow 6443/tcp`
    - `sudo ufw allow 2379:2380/tcp`
    - `sudo ufw allow 10250/tcp`
    - `sudo ufw allow 10256/tcp`
    - `sudo ufw allow 10257/tcp`
    - `sudo ufw allow 10259/tcp`
    - `sudo ufw allow 30000:32767/tcp`
    - `sudo ufw enable`
    - `sudo ufw reload`
- Installing container runtime ([containerd](https://kubernetes.io/docs/setup/production-environment/container-runtimes/)):
    - ```
        cat <<EOF | sudo tee /etc/sysctl.d/k8s.conf
        net.ipv4.ip_forward = 1
        EOF
      ```
    - `sudo sysctl --system`
    - `sudo apt install containerd`
    - `sudo systemctl daemon-reload`
    - `sudo systemctl enable --now containerd`
    - `sudo apt install runc`
    - `sudo nano /etc/containerd/config.toml`
    - ```
        [plugins]
          [plugins."io.containerd.grpc.v1.cri"]
          ...
            [plugins."io.containerd.grpc.v1.cri".containerd.runtimes.runc]
              [plugins."io.containerd.grpc.v1.cri".containerd.runtimes.runc.options]
                SystemdCgroup = true
      ```
    - `sudo systemctl restart containerd`
- Installing kubeadm, kubelet and kubectl
    - `sudo apt-get update`
    - `sudo apt-get install -y apt-transport-https ca-certificates curl gpg`
    - `curl -fsSL https://pkgs.k8s.io/core:/stable:/v1.32/deb/Release.key | sudo gpg --dearmor -o /etc/apt/keyrings/kubernetes-apt-keyring.gpg`
    - `echo 'deb [signed-by=/etc/apt/keyrings/kubernetes-apt-keyring.gpg] https://pkgs.k8s.io/core:/stable:/v1.32/deb/ /' | sudo tee /etc/apt/sources.list.d/kubernetes.list`
    - `sudo apt-get update`
    - `sudo apt-get install -y kubelet kubeadm kubectl`
    - `sudo apt-mark hold kubelet kubeadm kubectl`
    - `sudo systemctl enable --now kubelet`
- Creating a cluster with [kubeadm](https://kubernetes.io/docs/setup/production-environment/tools/kubeadm/create-cluster-kubeadm/)
    - `ip route show`
    - node-1:
        - `sudo kubeadm init` - ERROR (API server not healthy)
        - `sudo kubeadm reset`
        - `sudo kubeadm init` - ERROR (API server not healthy)
          ... missing api server pod name???
        - `sudo kubeadm reset`

___Pausing this for now. What a hassle...___
