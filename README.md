# FileSorter

FileSorter is a simple command line application written in JAVA. FileSorter sorts files by their file name extension (for example .java) into sub folders of the supplied target folder. The sub folders in the target are automatically generated according to the file extension.

## Usage

FileSorter can be run with [https://github.com/jbangdev/jbang](J'BANG) as follows.

1. Install [https://github.com/jbangdev/jbang](J'BANG) as described.
2. Issue the following command in your favorite command line environment: `jbang https://github.com/mabakach/FileSorter/blob/main/FileSorter.java'
3. Press 0 to trust FileSorter once or 1 to trust FileSorter for ever.

### Arguments

The following arguments are available:

`-h, --help        Show help message and exit.
 -i, --input=<inputDirectory>
                    Path to the unsorted files
 -o, --output=<outputDirectory>
                    Path where the sorted file should be copied to.
 -r, --recursive   Recursively process all subdirectories. Default = true
 -v, --verbose     Enable verbose output. Default = false
 -V, --version     Print version information and exit.`
