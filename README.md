# My Lab - Trying & Building "Stuff"

## Windows Server 2016 In-Place Upgrade

_aka Feature Update, [MS Learn](https://learn.microsoft.com/en-us/windows-server/get-started/perform-in-place-upgrade)_

- Machine: (Bare Metal) Intel Xeon X5650 (x2), 64 GB RAM, 5 TB on SSDs
- Running `Get-ComputerInfo -Property WindowsProductName`: __Windows Server 2016 Standard__
- Starting `setup.exe` from downloaded & mounted ISO image
- Selecting "Windows Server 2022 Standard (Desktop Experience)"
- Installing ...
- Duration: ~2 hours
- Running `Get-ComputerInfo -Property WindowsProductName`: __Windows Server 2022 Standard__
