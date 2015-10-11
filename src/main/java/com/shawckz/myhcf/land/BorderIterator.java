package com.shawckz.myhcf.land;

import java.util.Iterator;

public class BorderIterator implements Iterator<Coordinate> {
        private int x;
        private int z;
        private boolean next;
        private BorderDirection dir;
        int maxX;
        int maxZ;
        int minX;
        int minZ;
        
        public BorderIterator(final Claim claim, final int x1, final int z1, final int x2, final int z2) {
            this.next = true;
            this.dir = BorderDirection.POS_Z;
            this.maxX = claim.getMaximumPoint().getBlockX();
            this.maxZ = claim.getMaximumPoint().getBlockZ();
            this.minX = claim.getMinimumPoint().getBlockX();
            this.minZ = claim.getMinimumPoint().getBlockZ();
            this.x = Math.min(x1, x2);
            this.z = Math.min(z1, z2);
        }
        
        @Override
        public boolean hasNext() {
            return this.next;
        }
        
        @Override
        public Coordinate next() {
            if (this.dir == BorderDirection.POS_Z) {
                if (++this.z == this.maxZ) {
                    this.dir = BorderDirection.POS_X;
                }
            }
            else if (this.dir == BorderDirection.POS_X) {
                if (++this.x == this.maxX) {
                    this.dir = BorderDirection.NEG_Z;
                }
            }
            else if (this.dir == BorderDirection.NEG_Z) {
                if (--this.z == this.minZ) {
                    this.dir = BorderDirection.NEG_X;
                }
            }
            else if (this.dir == BorderDirection.NEG_X && --this.x == this.minX) {
                this.next = false;
            }
            return new Coordinate(this.x, this.z);
        }
        
        @Override
        public void remove() {
        }

    public enum BorderDirection
    {
        POS_X,
        POS_Z,
        NEG_X,
        NEG_Z;
    }

}