# Kubernetes: Simple Web Server Setup

- Create [Deployment](../../src/kubernetes/web-server-deployment.yaml)
- Create [Service](../../src/kubernetes/web-server-service.yaml)

- `kubectl apply -f .\src\kubernetes\web-server-deployment.yaml`
- `kubectl get pods -o wide`

  ```
  NAME                                     READY   STATUS                       RESTARTS   AGE   IP           NODE            NOMINATED NODE   READINESS GATES
  web-server-deployment-68dc899cf7-8f6ls   0/1     CreateContainerConfigError   0          12m   10.244.1.5   talos-f7e-hv7   <none>           <none>
  web-server-deployment-7bb587984b-fvvf9   1/1     Running                      0          20m   10.244.1.3   talos-f7e-hv7   <none>           <none>
  web-server-deployment-7bb587984b-fwqbc   1/1     Running                      0          20m   10.244.2.4   talos-y8g-oi1   <none>           <none>
  web-server-deployment-7bb587984b-wlqwr   1/1     Running                      0          20m   10.244.2.3   talos-y8g-oi1   <none>           <none>
  ```

- `kubectl apply -f .\src\kubernetes\web-server-service.yaml`
- `kubectl get services -o wide`

  ```
  NAME                 TYPE        CLUSTER-IP      EXTERNAL-IP   PORT(S)        AGE    SELECTOR
  kubernetes           ClusterIP   10.96.0.1       <none>        443/TCP        96m    <none>
  web-server-service   NodePort    10.109.153.26   <none>        80:30080/TCP   7m8s   app=web-server
  ```

- `start http://192.168.0.140:30080/`
