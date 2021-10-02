#!/usr/bin/env python
import sys


def read_from_input(file, splitter='\t'):
    for line in file:
        yield line.strip().split(splitter)


def main():
    # current mean, var and count
    mj = 0.0
    vj = 0.0
    cj = 0
    input = read_from_input(sys.stdin)
    for line in input:
        try:
            _, ck, mk, vk = line
            ck = int(ck)
            mk = float(mk)
            vk = float(vk)
            # update
            vj = (vj * cj + vk * ck) / (cj + ck) + cj * ck *\
                ((mj - mk) / (cj + ck)) ** 2
            mj = (mj * cj + mk * ck) / (cj + ck)
            cj += ck
        except (ValueError, ZeroDivisionError):
            pass

    sys.stdout.write('var\t{}\n'.format(vj))


if __name__ == '__main__':
    main()