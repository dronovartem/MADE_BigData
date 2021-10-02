#!/usr/bin/env python
import sys


def read_from_input(file, splitter='\t'):
    for line in file:
        yield line.strip().split(splitter)


def main():
    # current mean and count
    mj = 0.0
    cj = 0
    input = read_from_input(sys.stdin)
    for line in input:
        try:
            _, ck, mk = line
            mk = float(mk)
            ck = int(ck)
            # update
            mj = (mj * cj + mk * ck) / (cj + ck)
            cj += ck
        except (ValueError, ZeroDivisionError):
            pass

    sys.stdout.write('mean\t{}\n'.format(mj))


if __name__ == '__main__':
    main()
