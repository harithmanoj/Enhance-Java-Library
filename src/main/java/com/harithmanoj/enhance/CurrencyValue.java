//
//	This file is part of project Enhance Java Library
//
//	Copyright (C) 2021 Harith Manoj
//
//	Licensed under the Apache License, Version 2.0 (the "License");
//	you may not use this file except in compliance with the License.
//	You may obtain a copy of the License at
//
//       http://www.apache.org/licenses/LICENSE-2.0
//
//	Unless required by applicable law or agreed to in writing, software
//	distributed under the License is distributed on an "AS IS" BASIS,
//	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//	See the License for the specific language governing permissions and
//	limitations under the License.

package com.harithmanoj.enhance;

import java.util.Currency;

import static java.lang.Math.pow;

public class CurrencyValue {
    private int _rawValue;
    private int _fractionalSize;
    private int _padding;
    private String _code;

    public int getMain() {
        return _rawValue / ((int)pow(10,(_fractionalSize + _padding)));
    }

    public int getFractional() {
        return (_rawValue / ((int)pow(10, _padding))) % ((int)pow(10,_fractionalSize));
    }

    public double get() {
        return ((double)_rawValue) / pow(10,(_fractionalSize + _padding));
    }

    public int getRaw() {
        return _rawValue;
    }

    public int getFractionalSize() {
        return _fractionalSize;
    }

    public int getPadding() {
        return _padding;
    }

    public String getCode() {
        return _code;
    }

    public CurrencyValue add(CurrencyValue cr) {
        if (!cr._code.equals(_code)) {
            return null;
        } else {
            if(cr._padding > _padding) {
                return new CurrencyValue(
                        (_rawValue * (int) pow(10, cr._padding - _padding)) + cr._rawValue,
                        _fractionalSize,
                        _code,
                        cr._padding
                );
            } else {
                return new CurrencyValue(
                        (cr._rawValue * (int) pow(10, _padding - cr._padding)) + _rawValue,
                        _fractionalSize,
                        _code,
                        _padding
                );
            }
        }
    }

    public CurrencyValue sub(CurrencyValue cr) {
        if (!cr._code.equals(_code)) {
            return null;
        } else {
            if(cr._padding > _padding) {
                return new CurrencyValue(
                        (_rawValue * (int) pow(10, cr._padding - _padding)) - cr._rawValue,
                        _fractionalSize,
                        _code,
                        cr._padding
                );
            } else {
                return new CurrencyValue(
                        _rawValue - (cr._rawValue * (int) pow(10, _padding - cr._padding)),
                        _fractionalSize,
                        _code,
                        _padding
                );
            }
        }
    }

    public CurrencyValue percent(long numerator, long denominator) {
        return new CurrencyValue(
                (int)( _rawValue * numerator )/ (int)(denominator*100),
                _fractionalSize,
                _code,
                _padding
        );
    }

    public CurrencyValue() {
        _rawValue = 0;
        _fractionalSize = 2;
        _padding = 2;
        _code = "INR";
    }

    public CurrencyValue(Currency cr) {
        _rawValue = 0;
        _fractionalSize = cr.getDefaultFractionDigits();
        _padding = 2;
        _code = cr.getCurrencyCode();
    }

    public CurrencyValue(int raw) {
        _rawValue = raw;
        _fractionalSize = 2;
        _padding = 2;
        _code = "INR";
    }

    public CurrencyValue(int raw, Currency cr) {
        _rawValue = raw;
        _fractionalSize = cr.getDefaultFractionDigits();
        _padding = 2;
        _code = cr.getCurrencyCode();
    }

    public CurrencyValue(int raw, int padding) {
        _rawValue = raw;
        _fractionalSize = 2;
        _padding = padding;
        _code = "INR";
    }

    public CurrencyValue(int raw, Currency cr, int padding) {
        _rawValue = raw;
        _fractionalSize = cr.getDefaultFractionDigits();
        _padding = padding;
        _code = cr.getCurrencyCode();
    }

    public CurrencyValue(int raw, int fract, String code, int padd) {
        _rawValue = raw;
        _fractionalSize = fract;
        _padding = padd;
        _code = code;
    }



}
