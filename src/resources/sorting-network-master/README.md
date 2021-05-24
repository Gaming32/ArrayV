# sorting-network
Python script to check sorting networks and generate sorting network diagrams

## Usage

```
usage: sortingnetwork.py [-h] [-i inputfile] [-o [outputfile]] [-c] [-s [list]] [--svg [outputfile]]

optional arguments:
  -h, --help                                show this help message and exit
  -i inputfile, --input inputfile           specify a file containing comparison network definition
  -o [outputfile], --output [outputfile]    specify a file for saving the comparison network definition
  -c, --check                               check whether it is a sorting network
  --show-progress                           show percent complete while checking whether it is a
                                            sorting network
  -s [list], --sort [list]                  sorts the list using the input comparison network
  --svg [outputfile]                        generate SVG

```

Comparison networks can be specified like this: `0:1,2:3,0:2,1:3,1:2` and can either be loaded from a file using the `--input` argument or if no input file is specified, read from stdin.

Multiple lines can be used as well, to logically group the comparators at each depth. `0:1,2:3,0:2,1:3,1:2` is the same as this:
```
0:1,2:3
0:2,1:3
1:2
```

## Examples
##### Read a comparison network from a file called example.cn and check whether it is is a sorting network.
```
./sortingnetwork.py --input example.cn --check
```

##### Pipe a comparison network from stdin and check whether it is is a sorting network.
```
echo "0:1,2:3,0:2,1:3,1:2" | ./sortingnetwork.py --check
```

##### Read a comparison network from a file called example.cn and generate SVG to stdout.
```
./sortingnetwork.py --input example.cn --svg
```

##### Read a comparison network from a file called example.cn and generate SVG, saved to a file called output.svg.
```
./sortingnetwork.py --input example.cn --svg output.svg
```

##### Pipe the output to rsvg-convert to generate a PNG (or other format) instead of SVG.
(*rsvg-convert can be installed by using `sudo apt-get install librsvg2-bin` on Ubuntu.*)

4-input sorting network:
```
./sortingnetwork.py --input examples/4-input.cn --svg | rsvg-convert > examples/4-input.png
```

5-input sorting network:
```
./sortingnetwork.py --input examples/5-input.cn --svg | rsvg-convert > examples/5-input.png
```

##### Use a specified sorting network to sort a list.

Using a 4-input sorting network to sort a list containing 4 items:
```
./sortingnetwork.py --input examples/4-input.cn --sort 2,4,1,3
```
Outputs 
```
1
2
3
4
```

Using a 5-input sorting network to sort a list containing 5 items:
```
./sortingnetwork.py --input examples/5-input.cn --sort 5,2,4,1,3
```
Outputs
```
1
2
3
4
5
```

## Example sorting networks

### 4-Input

```text
0:1,2:3
0:2,1:3
1:2
```

![4-Input Sorting Network](https://github.com/brianpursley/sorting-network/blob/master/examples/4-input.png)

### 5-Input

```text
0:1,3:4
2:4
2:3,1:4
0:3
0:2,1:3
1:2
```

![5-Input Sorting Network](https://github.com/brianpursley/sorting-network/blob/master/examples/5-input.png)

### 16-Input

```text
0:1,2:3,4:5,6:7,8:9,10:11,12:13,14:15
0:2,1:3,4:6,5:7,8:10,9:11,12:14,13:15
0:4,1:5,2:6,3:7,8:12,9:13,10:14,11:15
0:8,1:9,2:10,3:11,4:12,5:13,6:14,7:15
5:10,6:9,3:12,13:14,7:11,1:2,4:8
1:4,7:13,2:8,11:14
2:4,5:6,9:10,11:13,3:8,7:12
6:8,10:12,3:5,7:9
3:4,5:6,7:8,9:10,11:12
6:7,8:9
```

![16-Input Sorting Network](https://github.com/brianpursley/sorting-network/blob/master/examples/16-input.png)

