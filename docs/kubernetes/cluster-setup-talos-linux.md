# Kubernetes Cluster Setup using `Talos Linux`

_using [Talos Linux on Hyper-V](https://www.talos.dev/v1.9/talos-guides/install/virtualized-platforms/hyper-v/)_

__Duration: ~2 hours with success__

## On Hyper-V Server:
- Download [`metal-amd64.iso`](https://github.com/siderolabs/talos/releases/)
- Download [`New-TalosVM.psm1`](https://github.com/nebula-it/New-TalosVM/blob/main/Talos/1.0.0/Talos.psm1)
- `New-TalosVM -VMNamePrefix talos-control -CPUCount 2 -StartupMemory 4GB -SwitchName LAB -TalosISOPath 'C:\Users\Administrator\Downloads\Operating System ISOs\metal-amd64.iso' -NumberOfVMs 1 -VMDestinationBasePath 'G:\Kubernetes\Talos'`
    - _NOTE: The virtual switch must already exist!_
- `New-TalosVM -VMNamePrefix talos-worker -CPUCount 4 -StartupMemory 8GB -SwitchName LAB -TalosISOPath 'C:\Users\Administrator\Downloads\Operating System ISOs\metal-amd64.iso' -NumberOfVMs 2 -VMDestinationBasePath 'G:\Kubernetes\Talos' -StorageVHDSize 50GB`

## On Client:
- Download [`talosctl`](https://github.com/siderolabs/talos/releases/)
- `$CONTROL_PLANE_IP='192.168.0.137'`
- `talosctl gen config talos-cluster https://$($CONTROL_PLANE_IP):6443 --output-dir .`
- `talosctl apply-config --insecure --nodes $CONTROL_PLANE_IP --file .\controlplane.yaml`
- `talosctl apply-config --insecure --nodes 192.168.0.139 --file .\worker.yaml`
- `talosctl apply-config --insecure --nodes 192.168.0.140 --file .\worker.yaml`
- `talosctl config endpoint $CONTROL_PLANE_IP --talosconfig .\talosconfig`
- `talosctl config node $CONTROL_PLANE_IP --talosconfig .\talosconfig`
- `talosctl bootstrap --talosconfig .\talosconfig`
- `talosctl kubeconfig . --talosconfig .\talosconfig`

- `mv $HOME/.talos/config $HOME/.talos/config.bak`
- `cp ./talosconfig $HOME/.talos/config`

- `mv $HOME/.kube/config $HOME/.kube/config.bak`
- `cp ./kubeconfig $HOME/.kube/config`

___Wow, that was easy!___
