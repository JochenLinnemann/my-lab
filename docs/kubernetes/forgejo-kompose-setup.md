# Kubernetes: Forgejo Setup using Kompose

Refer to [Forgejo with PostgreSQL database](https://forgejo.org/docs/latest/admin/installation-docker/#postgresql-database) and [Docker Compose File to Kubernetes Resources](https://kubernetes.io/docs/tasks/configure-pod-container/translate-compose-kubernetes/).

## Tool Preparation

- `kubectl version` --> version mismatch (client 1.30.0, server 1.32.0)
- `mkdir bin`
- `cd bin`
- `curl.exe -LO "https://dl.k8s.io/release/v1.32.0/bin/windows/amd64/kubectl.exe"`
- `curl.exe -L https://github.com/kubernetes/kompose/releases/download/v1.32.0/kompose-windows-amd64.exe -o kompose.exe`
- `cd ..`
- `.\bin\kubectl version`
- `.\bin\kompose version`

## File Conversion

- Download [docker-compose file](../../src/kubernetes/forgejo/docker-compose.yaml)

- `cd ./src/kubernetes/forgejo`
- `..\..\..\bin\kompose convert -f ./docker-compose.yaml`

  ```
  WARN Service "db" won't be created because 'ports' is not specified
  WARN Volume mount on the host   "./my-lab/src/kubernetes/forgejo/postgres" isn't   supported - ignoring path on the host
  WARN Volume mount on the host   "./my-lab/src/kubernetes/forgejo/forgejo" isn't   supported - ignoring path on the host
  WARN Volume mount on the host "/etc/timezone" isn't supported - ignoring path on the host
  WARN Volume mount on the host "/etc/localtime" isn't supported - ignoring path on the host
  INFO Kubernetes file "server-service.yaml" created
  INFO Kubernetes file "db-deployment.yaml" created
  INFO Kubernetes file "db-claim0-persistentvolumeclaim.yaml" created
  INFO Kubernetes file "server-deployment.yaml" created
  INFO Kubernetes file "server-claim0-persistentvolumeclaim.yaml" created
  INFO Kubernetes file "server-claim1-persistentvolumeclaim.yaml" created
  INFO Kubernetes file "server-claim2-persistentvolumeclaim.yaml" created
  ```

- `.\bin\kubectl apply -f .\src\kubernetes\forgejo\db-claim0-persistentvolumeclaim.yaml`
- `.\bin\kubectl apply -f .\src\kubernetes\forgejo\db-deployment.yaml`
- `.\bin\kubectl apply -f .\src\kubernetes\forgejo\server-claim0-persistentvolumeclaim.yaml`
- `.\bin\kubectl apply -f .\src\kubernetes\forgejo\server-claim1-persistentvolumeclaim.yaml`
- `.\bin\kubectl apply -f .\src\kubernetes\forgejo\server-claim2-persistentvolumeclaim.yaml`
- `.\bin\kubectl apply -f .\src\kubernetes\forgejo\server-deployment.yaml`
- `.\bin\kubectl apply -f .\src\kubernetes\forgejo\server-service.yaml`
- `.\bin\kubectl get services -o wide`

  ```
  NAME                 TYPE        CLUSTER-IP      EXTERNAL-IP   PORT(S)           AGE   SELECTOR
  kubernetes           ClusterIP   10.96.0.1       <none>        443/TCP           27d   <none>
  server               ClusterIP   10.96.248.179   <none>        3000/TCP,22/TCP   55s   io.kompose.service=server
  web-server-service   NodePort    10.109.153.26   <none>        80:30080/TCP      27d   app=web-server
  ```

- `.\bin\kubectl get pods -o wide`

  ```
  NAME                                     READY   STATUS                       RESTARTS     AGE     IP           NODE            NOMINATED NODE   READINESS GATES
  db-6bc99558d8-hhbrv                      0/1     Pending                      0            6m18s   <none>       <none>          <none>           <none>
  server-564dc4fb4b-h2jcd                  0/1     Pending                      0            102s    <none>       <none>          <none>           <none>
  web-server-deployment-68dc899cf7-8f6ls   0/1     CreateContainerConfigError   0            27d     10.244.1.7   talos-f7e-hv7   <none>           <none>
  web-server-deployment-7bb587984b-fvvf9   1/1     Running                      1 (8d ago)   27d     10.244.1.8   talos-f7e-hv7   <none>           <none>
  web-server-deployment-7bb587984b-fwqbc   1/1     Running                      1 (8d ago)   27d     10.244.2.7   talos-y8g-oi1   <none>           <none>
  web-server-deployment-7bb587984b-wlqwr   1/1     Running                      1 (8d ago)   27d     10.244.2.5   talos-y8g-oi1   <none>           <none>
  ```

- `.\bin\kubectl describe pod db-6bc99558d8-hhbrv`

  ```
  <snip>

  Events:
    Type     Reason            Age                  From               Message
    ----     ------            ----                 ----               -------
    Warning  FailedScheduling  11m                  default-scheduler  0/3 nodes are available: pod has unbound immediate PersistentVolumeClaims. preemption: 0/3 nodes are available: 3 Preemption is not helpful for scheduling.
    Warning  FailedScheduling  76s (x2 over 6m16s)  default-scheduler  0/3 nodes are available: pod has unbound immediate PersistentVolumeClaims. preemption: 0/3 nodes are available: 3 Preemption is not helpful for scheduling.
  ```

## TODO

- [ ] Research PersistentVolumes on bare metal
