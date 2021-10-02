#!/usr/bin/env python
import sys


DEFAULT_TARGET_COLUMN_ID = -7


def read_from_input(file, splitter=','):
    for line in file:
        yield line.split(splitter)


def main():
    mean = 0.0
    var = 0.0
    count = 0
    input = read_from_input(sys.stdin)
    for line in input:
        try:
            price = float(line[DEFAULT_TARGET_COLUMN_ID])
            mean += price
            var += price * price
            count += 1
        except (ValueError, IndexError):
            pass
    
    mean /= count
    var = var / count - mean ** 2

    sys.stdout.write('1\t{}\t{}\t{}\n'.format(count, mean, var))


if __name__ == '__main__':
    main()