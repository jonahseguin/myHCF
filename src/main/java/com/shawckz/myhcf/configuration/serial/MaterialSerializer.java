/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.
 * Thank you.
 */

package com.shawckz.myhcf.configuration.serial;

import com.shawckz.myhcf.configuration.AbstractSerializer;
import com.shawckz.myhcf.util.HCFException;
import org.bukkit.Material;

/**
 * Created by Jonah Seguin on 1/24/2016.
 *
 * @author Shawckz
 *         Shawckz.com
 */
public class MaterialSerializer extends AbstractSerializer<Material> {

    @Override
    public String toString(Material data) {
        return data.toString();
    }

    @Override
    public Material fromString(Object data) {
        if (data instanceof String) {
            String s = (String) data;
            return Material.valueOf(s.toUpperCase());
        }
        throw new HCFException("MaterialSerializer data is not a string");
    }
}
