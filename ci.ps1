

function Update-InstantModules {
    param (
        [parameter(ValueFromPipeline=$True)] 
        [string]$name
    )

    $sourcePath      = (Resolve-Path "./${name}-module/features/${name}").Path
    $destinationPath = "./integration/features/${name}"

    if ($destinationPath | Test-Path) {
        $destinationPath | rm -Force -Recurse -Verbose -ErrorAction Continue
    }

    cp $sourcePath  $destinationPath -Recurse -Force -Exclude 'build'
}

@('map', 'gauge', 'orchestrator') | %{ $_ | Update-InstantModules }