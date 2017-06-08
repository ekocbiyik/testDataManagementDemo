package com.ekocbiyik.tdmdemo.utils;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.Random;

/**
 * Created by enbiya on 04.12.2016.
 */
public class EncryptionUtils {

    private static final String SHA256FUNC = "function SHA256(s){\n" +
            "var chrsz   = 8;\n" +
            "var hexcase = 0;\n" +
            "function safe_add (x, y) {\n" +
            "var lsw = (x & 0xFFFF) + (y & 0xFFFF);\n" +
            "var msw = (x >> 16) + (y >> 16) + (lsw >> 16);\n" +
            "return (msw << 16) | (lsw & 0xFFFF);\n" +
            "}\n" +
            "function S (X, n) { return ( X >>> n ) | (X << (32 - n)); }\n" +
            "function R (X, n) { return ( X >>> n ); }\n" +
            "function Ch(x, y, z) { return ((x & y) ^ ((~x) & z)); }\n" +
            "function Maj(x, y, z) { return ((x & y) ^ (x & z) ^ (y & z)); }\n" +
            "function Sigma0256(x) { return (S(x, 2) ^ S(x, 13) ^ S(x, 22)); }\n" +
            "function Sigma1256(x) { return (S(x, 6) ^ S(x, 11) ^ S(x, 25)); }\n" +
            "function Gamma0256(x) { return (S(x, 7) ^ S(x, 18) ^ R(x, 3)); }\n" +
            "function Gamma1256(x) { return (S(x, 17) ^ S(x, 19) ^ R(x, 10)); }\n" +
            "function core_sha256 (m, l) {\n" +
            "var K = new Array(0x428A2F98, 0x71374491, 0xB5C0FBCF, 0xE9B5DBA5, 0x3956C25B, 0x59F111F1, 0x923F82A4, 0xAB1C5ED5, 0xD807AA98, 0x12835B01, 0x243185BE, 0x550C7DC3, 0x72BE5D74, " +
            "0x80DEB1FE, 0x9BDC06A7, 0xC19BF174, 0xE49B69C1, 0xEFBE4786, 0xFC19DC6, 0x240CA1CC, 0x2DE92C6F, 0x4A7484AA, 0x5CB0A9DC, 0x76F988DA, 0x983E5152, 0xA831C66D, 0xB00327C8, 0xBF597FC7, " +
            "0xC6E00BF3, 0xD5A79147, 0x6CA6351, 0x14292967, 0x27B70A85, 0x2E1B2138, 0x4D2C6DFC, 0x53380D13, 0x650A7354, 0x766A0ABB, 0x81C2C92E, 0x92722C85, 0xA2BFE8A1, 0xA81A664B, 0xC24B8B70, " +
            "0xC76C51A3, 0xD192E819, 0xD6990624, 0xF40E3585, 0x106AA070, 0x19A4C116, 0x1E376C08, 0x2748774C, 0x34B0BCB5, 0x391C0CB3, 0x4ED8AA4A, 0x5B9CCA4F, 0x682E6FF3, 0x748F82EE, 0x78A5636F, " +
            "0x84C87814, 0x8CC70208, 0x90BEFFFA, 0xA4506CEB, 0xBEF9A3F7, 0xC67178F2);\n" +
            "var HASH = new Array(0x6A09E667, 0xBB67AE85, 0x3C6EF372, 0xA54FF53A, 0x510E527F, 0x9B05688C, 0x1F83D9AB, 0x5BE0CD19);\n" +
            "var W = new Array(64);\n" +
            "var a, b, c, d, e, f, g, h, i, j;\n" +
            "var T1, T2;\n" +
            "m[l >> 5] |= 0x80 << (24 - l % 32);\n" +
            "m[((l + 64 >> 9) << 4) + 15] = l;\n" +
            "for ( var i = 0; i<m.length; i+=16 ) {\n" +
            "a = HASH[0];\n" +
            "b = HASH[1];\n" +
            "c = HASH[2];\n" +
            "d = HASH[3];\n" +
            "e = HASH[4];\n" +
            "f = HASH[5];\n" +
            "g = HASH[6];\n" +
            "h = HASH[7];\n" +
            "for ( var j = 0; j<64; j++) {\n" +
            "if (j < 16) W[j] = m[j + i];\n" +
            "else W[j] = safe_add(safe_add(safe_add(Gamma1256(W[j - 2]), W[j - 7]), Gamma0256(W[j - 15])), W[j - 16]);\n" +
            "T1 = safe_add(safe_add(safe_add(safe_add(h, Sigma1256(e)), Ch(e, f, g)), K[j]), W[j]);\n" +
            "T2 = safe_add(Sigma0256(a), Maj(a, b, c));\n" +
            "h = g;\n" +
            "g = f;\n" +
            "f = e;\n" +
            "e = safe_add(d, T1);\n" +
            "d = c;\n" +
            "c = b;\n" +
            "b = a;\n" +
            "a = safe_add(T1, T2);\n" +
            "}\n" +
            "HASH[0] = safe_add(a, HASH[0]);\n" +
            "HASH[1] = safe_add(b, HASH[1]);\n" +
            "HASH[2] = safe_add(c, HASH[2]);\n" +
            "HASH[3] = safe_add(d, HASH[3]);\n" +
            "HASH[4] = safe_add(e, HASH[4]);\n" +
            "HASH[5] = safe_add(f, HASH[5]);\n" +
            "HASH[6] = safe_add(g, HASH[6]);\n" +
            "HASH[7] = safe_add(h, HASH[7]);\n" +
            "}\n" +
            "return HASH;\n" +
            "}\n" +
            "function str2binb (str) {\n" +
            "var bin = Array();\n" +
            "var mask = (1 << chrsz) - 1;\n" +
            "for(var i = 0; i < str.length * chrsz; i += chrsz) {\n" +
            "bin[i>>5] |= (str.charCodeAt(i / chrsz) & mask) << (24 - i%32);\n" +
            "}\n" +
            "return bin;\n" +
            "}\n" +
            "function Utf8Encode(string) {\n" +
            "string = string.replace(/\\r\\n/g,\"\\n\");\n" +
            "var utftext = \"\";\n" +
            "for (var n = 0; n < string.length; n++) {\n" +
            "var c = string.charCodeAt(n);\n" +
            "if (c < 128) {\n" +
            "utftext += String.fromCharCode(c);\n" +
            "}\n" +
            "else if((c > 127) && (c < 2048)) {\n" +
            "utftext += String.fromCharCode((c >> 6) | 192);\n" +
            "utftext += String.fromCharCode((c & 63) | 128);\n" +
            "}\n" +
            "else {\n" +
            "utftext += String.fromCharCode((c >> 12) | 224);\n" +
            "utftext += String.fromCharCode(((c >> 6) & 63) | 128);\n" +
            "utftext += String.fromCharCode((c & 63) | 128);\n" +
            "}\n" +
            "}\n" +
            "return utftext;\n" +
            "}\n" +
            "function binb2hex (binarray) {\n" +
            "var hex_tab = hexcase ? \"0123456789ABCDEF\" : \"0123456789abcdef\";\n" +
            "var str = \"\";\n" +
            "for(var i = 0; i < binarray.length * 4; i++) {\n" +
            "str += hex_tab.charAt((binarray[i>>2] >> ((3 - i%4)*8+4)) & 0xF) +\n" +
            "hex_tab.charAt((binarray[i>>2] >> ((3 - i%4)*8  )) & 0xF);\n" +
            "}\n" +
            "return str;\n" +
            "}\n" +
            "s = Utf8Encode(s);\n" +
            "return binb2hex(core_sha256(str2binb(s), s.length * chrsz));\n" +
            "}";
    private static final String base64EncodeChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
    private static final String BASE64ENCODEFUNC = "function base64encode(str,base64EncodeChars) {\n" +
            "var out, i, len;\n" +
            "var c1, c2, c3;\n" +
            "len = str.length;\n" +
            "i = 0;\n" +
            "out = \"\";\n" +
            "while(i < len) {\n" +
            "c1 = str.charCodeAt(i++) & 0xff;\n" +
            "if(i == len)\n" +
            "{\n" +
            "out += base64EncodeChars.charAt(c1 >> 2);\n" +
            "out += base64EncodeChars.charAt((c1 & 0x3) << 4);\n" +
            "out += \"==\";\n" +
            "break;\n" +
            "}\n" +
            "c2 = str.charCodeAt(i++);\n" +
            "if(i == len)\n" +
            "{\n" +
            "out += base64EncodeChars.charAt(c1 >> 2);\n" +
            "out += base64EncodeChars.charAt(((c1 & 0x3)<< 4) | ((c2 & 0xF0) >> 4));\n" +
            "out += base64EncodeChars.charAt((c2 & 0xF) << 2);\n" +
            "out += \"=\";\n" +
            "break;\n" +
            "}\n" +
            "c3 = str.charCodeAt(i++);\n" +
            "out += base64EncodeChars.charAt(c1 >> 2);\n" +
            "out += base64EncodeChars.charAt(((c1 & 0x3)<< 4) | ((c2 & 0xF0) >> 4));\n" +
            "out += base64EncodeChars.charAt(((c2 & 0xF) << 2) | ((c3 & 0xC0) >>6));\n" +
            "out += base64EncodeChars.charAt(c3 & 0x3F);\n" +
            "}\n" +
            "return out;\n" +
            "}";
    private static final String HEXMD5FUNC = "function hexMd5(s){\n" +
            "var hexcase = 0;  /* hex output format. 0 - lowercase; 1 - uppercase        */\n" +
            "var b64pad  = \"\"; /* base-64 pad character. \"=\" for strict RFC compliance   */\n" +
            "var chrsz   = 8;  /* bits per input character. 8 - ASCII; 16 - Unicode      */\n" +
            "\n" +
            "/*\n" +
            " * These are the functions you'll usually want to call\n" +
            " * They take string arguments and return either hex or base-64 encoded strings\n" +
            " */\n" +
            "function hex_md5(s){ return binl2hex(core_md5(str2binl(s), s.length * chrsz));}\n" +
            "function b64_md5(s){ return binl2b64(core_md5(str2binl(s), s.length * chrsz));}\n" +
            "function str_md5(s){ return binl2str(core_md5(str2binl(s), s.length * chrsz));}\n" +
            "function hex_hmac_md5(key, data) { return binl2hex(core_hmac_md5(key, data)); }\n" +
            "function b64_hmac_md5(key, data) { return binl2b64(core_hmac_md5(key, data)); }\n" +
            "function str_hmac_md5(key, data) { return binl2str(core_hmac_md5(key, data)); }\n" +
            "\n" +
            "/* \n" +
            " * Perform a simple self-test to see if the VM is working \n" +
            " */\n" +
            "\n" +
            "\n" +
            "/*\n" +
            " * Calculate the MD5 of an array of little-endian words, and a bit length\n" +
            " */\n" +
            "function core_md5(x, len)\n" +
            "{\n" +
            "  /* append padding */\n" +
            "  x[len >> 5] |= 0x80 << ((len) % 32);\n" +
            "  x[(((len + 64) >>> 9) << 4) + 14] = len;\n" +
            "  \n" +
            "  var a =  1732584193;\n" +
            "  var b = -271733879;\n" +
            "  var c = -1732584194;\n" +
            "  var d =  271733878;\n" +
            "\n" +
            "  for(var i = 0; i < x.length; i += 16)\n" +
            "  {\n" +
            "    var olda = a;\n" +
            "    var oldb = b;\n" +
            "    var oldc = c;\n" +
            "    var oldd = d;\n" +
            " \n" +
            "    a = md5_ff(a, b, c, d, x[i+ 0], 7 , -680876936);\n" +
            "    d = md5_ff(d, a, b, c, x[i+ 1], 12, -389564586);\n" +
            "    c = md5_ff(c, d, a, b, x[i+ 2], 17,  606105819);\n" +
            "    b = md5_ff(b, c, d, a, x[i+ 3], 22, -1044525330);\n" +
            "    a = md5_ff(a, b, c, d, x[i+ 4], 7 , -176418897);\n" +
            "    d = md5_ff(d, a, b, c, x[i+ 5], 12,  1200080426);\n" +
            "    c = md5_ff(c, d, a, b, x[i+ 6], 17, -1473231341);\n" +
            "    b = md5_ff(b, c, d, a, x[i+ 7], 22, -45705983);\n" +
            "    a = md5_ff(a, b, c, d, x[i+ 8], 7 ,  1770035416);\n" +
            "    d = md5_ff(d, a, b, c, x[i+ 9], 12, -1958414417);\n" +
            "    c = md5_ff(c, d, a, b, x[i+10], 17, -42063);\n" +
            "    b = md5_ff(b, c, d, a, x[i+11], 22, -1990404162);\n" +
            "    a = md5_ff(a, b, c, d, x[i+12], 7 ,  1804603682);\n" +
            "    d = md5_ff(d, a, b, c, x[i+13], 12, -40341101);\n" +
            "    c = md5_ff(c, d, a, b, x[i+14], 17, -1502002290);\n" +
            "    b = md5_ff(b, c, d, a, x[i+15], 22,  1236535329);\n" +
            "\n" +
            "    a = md5_gg(a, b, c, d, x[i+ 1], 5 , -165796510);\n" +
            "    d = md5_gg(d, a, b, c, x[i+ 6], 9 , -1069501632);\n" +
            "    c = md5_gg(c, d, a, b, x[i+11], 14,  643717713);\n" +
            "    b = md5_gg(b, c, d, a, x[i+ 0], 20, -373897302);\n" +
            "    a = md5_gg(a, b, c, d, x[i+ 5], 5 , -701558691);\n" +
            "    d = md5_gg(d, a, b, c, x[i+10], 9 ,  38016083);\n" +
            "    c = md5_gg(c, d, a, b, x[i+15], 14, -660478335);\n" +
            "    b = md5_gg(b, c, d, a, x[i+ 4], 20, -405537848);\n" +
            "    a = md5_gg(a, b, c, d, x[i+ 9], 5 ,  568446438);\n" +
            "    d = md5_gg(d, a, b, c, x[i+14], 9 , -1019803690);\n" +
            "    c = md5_gg(c, d, a, b, x[i+ 3], 14, -187363961);\n" +
            "    b = md5_gg(b, c, d, a, x[i+ 8], 20,  1163531501);\n" +
            "    a = md5_gg(a, b, c, d, x[i+13], 5 , -1444681467);\n" +
            "    d = md5_gg(d, a, b, c, x[i+ 2], 9 , -51403784);\n" +
            "    c = md5_gg(c, d, a, b, x[i+ 7], 14,  1735328473);\n" +
            "    b = md5_gg(b, c, d, a, x[i+12], 20, -1926607734);\n" +
            "\n" +
            "    a = md5_hh(a, b, c, d, x[i+ 5], 4 , -378558);\n" +
            "    d = md5_hh(d, a, b, c, x[i+ 8], 11, -2022574463);\n" +
            "    c = md5_hh(c, d, a, b, x[i+11], 16,  1839030562);\n" +
            "    b = md5_hh(b, c, d, a, x[i+14], 23, -35309556);\n" +
            "    a = md5_hh(a, b, c, d, x[i+ 1], 4 , -1530992060);\n" +
            "    d = md5_hh(d, a, b, c, x[i+ 4], 11,  1272893353);\n" +
            "    c = md5_hh(c, d, a, b, x[i+ 7], 16, -155497632);\n" +
            "    b = md5_hh(b, c, d, a, x[i+10], 23, -1094730640);\n" +
            "    a = md5_hh(a, b, c, d, x[i+13], 4 ,  681279174);\n" +
            "    d = md5_hh(d, a, b, c, x[i+ 0], 11, -358537222);\n" +
            "    c = md5_hh(c, d, a, b, x[i+ 3], 16, -722521979);\n" +
            "    b = md5_hh(b, c, d, a, x[i+ 6], 23,  76029189);\n" +
            "    a = md5_hh(a, b, c, d, x[i+ 9], 4 , -640364487);\n" +
            "    d = md5_hh(d, a, b, c, x[i+12], 11, -421815835);\n" +
            "    c = md5_hh(c, d, a, b, x[i+15], 16,  530742520);\n" +
            "    b = md5_hh(b, c, d, a, x[i+ 2], 23, -995338651);\n" +
            "\n" +
            "    a = md5_ii(a, b, c, d, x[i+ 0], 6 , -198630844);\n" +
            "    d = md5_ii(d, a, b, c, x[i+ 7], 10,  1126891415);\n" +
            "    c = md5_ii(c, d, a, b, x[i+14], 15, -1416354905);\n" +
            "    b = md5_ii(b, c, d, a, x[i+ 5], 21, -57434055);\n" +
            "    a = md5_ii(a, b, c, d, x[i+12], 6 ,  1700485571);\n" +
            "    d = md5_ii(d, a, b, c, x[i+ 3], 10, -1894986606);\n" +
            "    c = md5_ii(c, d, a, b, x[i+10], 15, -1051523);\n" +
            "    b = md5_ii(b, c, d, a, x[i+ 1], 21, -2054922799);\n" +
            "    a = md5_ii(a, b, c, d, x[i+ 8], 6 ,  1873313359);\n" +
            "    d = md5_ii(d, a, b, c, x[i+15], 10, -30611744);\n" +
            "    c = md5_ii(c, d, a, b, x[i+ 6], 15, -1560198380);\n" +
            "    b = md5_ii(b, c, d, a, x[i+13], 21,  1309151649);\n" +
            "    a = md5_ii(a, b, c, d, x[i+ 4], 6 , -145523070);\n" +
            "    d = md5_ii(d, a, b, c, x[i+11], 10, -1120210379);\n" +
            "    c = md5_ii(c, d, a, b, x[i+ 2], 15,  718787259);\n" +
            "    b = md5_ii(b, c, d, a, x[i+ 9], 21, -343485551);\n" +
            "\n" +
            "    a = safe_add(a, olda);\n" +
            "    b = safe_add(b, oldb);\n" +
            "    c = safe_add(c, oldc);\n" +
            "    d = safe_add(d, oldd);\n" +
            "  }\n" +
            "  return Array(a, b, c, d);\n" +
            "  \n" +
            "}\n" +
            "\n" +
            "/*\n" +
            " * These functions implement the four basic operations the algorithm uses.\n" +
            " */\n" +
            "function md5_cmn(q, a, b, x, s, t)\n" +
            "{\n" +
            "  return safe_add(bit_rol(safe_add(safe_add(a, q), safe_add(x, t)), s),b);\n" +
            "}\n" +
            "function md5_ff(a, b, c, d, x, s, t)\n" +
            "{\n" +
            "  return md5_cmn((b & c) | ((~b) & d), a, b, x, s, t);\n" +
            "}\n" +
            "function md5_gg(a, b, c, d, x, s, t)\n" +
            "{\n" +
            "  return md5_cmn((b & d) | (c & (~d)), a, b, x, s, t);\n" +
            "}\n" +
            "function md5_hh(a, b, c, d, x, s, t)\n" +
            "{\n" +
            "  return md5_cmn(b ^ c ^ d, a, b, x, s, t);\n" +
            "}\n" +
            "function md5_ii(a, b, c, d, x, s, t)\n" +
            "{\n" +
            "  return md5_cmn(c ^ (b | (~d)), a, b, x, s, t);\n" +
            "}\n" +
            "\n" +
            "/*\n" +
            " * Calculate the HMAC-MD5, of a key and some data\n" +
            " */\n" +
            "function core_hmac_md5(key, data)\n" +
            "{\n" +
            "  var bkey = str2binl(key);\n" +
            "  if(bkey.length > 16) bkey = core_md5(bkey, key.length * chrsz);\n" +
            "\n" +
            "  var ipad = Array(16), opad = Array(16);\n" +
            "  for(var i = 0; i < 16; i++) \n" +
            "  {\n" +
            "    ipad[i] = bkey[i] ^ 0x36363636;\n" +
            "    opad[i] = bkey[i] ^ 0x5C5C5C5C;\n" +
            "  }\n" +
            "\n" +
            "  var hash = core_md5(ipad.concat(str2binl(data)), 512 + data.length * chrsz);\n" +
            "  return core_md5(opad.concat(hash), 512 + 128);\n" +
            "}\n" +
            "\n" +
            "/*\n" +
            " * Add integers, wrapping at 2^32. This uses 16-bit operations internally\n" +
            " * to work around bugs in some JS interpreters.\n" +
            " */\n" +
            "function safe_add(x, y)\n" +
            "{\n" +
            "  var lsw = (x & 0xFFFF) + (y & 0xFFFF);\n" +
            "  var msw = (x >> 16) + (y >> 16) + (lsw >> 16);\n" +
            "  return (msw << 16) | (lsw & 0xFFFF);\n" +
            "}\n" +
            "\n" +
            "/*\n" +
            " * Bitwise rotate a 32-bit number to the left.\n" +
            " */\n" +
            "function bit_rol(num, cnt)\n" +
            "{\n" +
            "  return (num << cnt) | (num >>> (32 - cnt));\n" +
            "}\n" +
            "\n" +
            "/*\n" +
            " * Convert a string to an array of little-endian words\n" +
            " * If chrsz is ASCII, characters >255 have their hi-byte silently ignored.\n" +
            " */\n" +
            "function str2binl(str)\n" +
            "{\n" +
            "  var bin = Array();\n" +
            "  var mask = (1 << chrsz) - 1;\n" +
            "  for(var i = 0; i < str.length * chrsz; i += chrsz)\n" +
            "    bin[i>>5] |= (str.charCodeAt(i / chrsz) & mask) << (i%32);\n" +
            "  return bin;\n" +
            "}\n" +
            "\n" +
            "/*\n" +
            " * Convert an array of little-endian words to a string\n" +
            " */\n" +
            "function binl2str(bin)\n" +
            "{\n" +
            "  var str = \"\";\n" +
            "  var mask = (1 << chrsz) - 1;\n" +
            "  for(var i = 0; i < bin.length * 32; i += chrsz)\n" +
            "    str += String.fromCharCode((bin[i>>5] >>> (i % 32)) & mask);\n" +
            "  return str;\n" +
            "}\n" +
            "\n" +
            "/*\n" +
            " * Convert an array of little-endian words to a hex string.\n" +
            " */\n" +
            "function binl2hex(binarray)\n" +
            "{\n" +
            "  var hex_tab = hexcase ? \"0123456789ABCDEF\" : \"0123456789abcdef\";\n" +
            "  var str = \"\";\n" +
            "  for(var i = 0; i < binarray.length * 4; i++)\n" +
            "  {\n" +
            "    str += hex_tab.charAt((binarray[i>>2] >> ((i%4)*8+4)) & 0xF) +\n" +
            "           hex_tab.charAt((binarray[i>>2] >> ((i%4)*8  )) & 0xF);\n" +
            "  }\n" +
            "  return str;\n" +
            "}\n" +
            "\n" +
            "/*\n" +
            " * Convert an array of little-endian words to a base-64 string\n" +
            " */\n" +
            "function binl2b64(binarray)\n" +
            "{\n" +
            "  var tab = \"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/\";\n" +
            "  var str = \"\";\n" +
            "  for(var i = 0; i < binarray.length * 4; i += 3)\n" +
            "  {\n" +
            "    var triplet = (((binarray[i   >> 2] >> 8 * ( i   %4)) & 0xFF) << 16)\n" +
            "                | (((binarray[i+1 >> 2] >> 8 * ((i+1)%4)) & 0xFF) << 8 )\n" +
            "                |  ((binarray[i+2 >> 2] >> 8 * ((i+2)%4)) & 0xFF);\n" +
            "    for(var j = 0; j < 4; j++)\n" +
            "    {\n" +
            "      if(i * 8 + j * 6 > binarray.length * 32) str += b64pad;\n" +
            "      else str += tab.charAt((triplet >> 6*(3-j)) & 0x3F);\n" +
            "    }\n" +
            "  }\n" +
            "  return str;\n" +
            "}\n" +
            "return hex_md5(s);\n" +
            "}";
    public static ScriptEngineManager manager = new ScriptEngineManager();
    public static ScriptEngine engine = manager.getEngineByName("JavaScript");
    private static String RSAFUNC = "" +
            "" +
            "\n" +
            "\n" +
            "// Bits per digit\n" +
            "var dbits;\n" +
            "\n" +
            "// JavaScript engine analysis\n" +
            "var canary = 0xdeadbeefcafe;\n" +
            "var j_lm = ((canary&0xffffff)==0xefcafe);\n" +
            "\n" +
            "// (public) Constructor\n" +
            "function BigInteger(a,b,c) {\n" +
            "  if(a != null)\n" +
            "    if(\"number\" == typeof a) this.fromNumber(a,b,c);\n" +
            "    else if(b == null && \"string\" != typeof a) this.fromString(a,256);\n" +
            "    else this.fromString(a,b);\n" +
            "}\n" +
            "\n" +
            "// return new, unset BigInteger\n" +
            "function nbi() { return new BigInteger(null); }\n" +
            "\n" +
            "// am: Compute w_j += (x*this_i), propagate carries,\n" +
            "// c is initial carry, returns final carry.\n" +
            "// c < 3*dvalue, x < 2*dvalue, this_i < dvalue\n" +
            "// We need to select the fastest one that works in this environment.\n" +
            "\n" +
            "// am1: use a single mult and divide to get the high bits,\n" +
            "// max digit bits should be 26 because\n" +
            "// max internal value = 2*dvalue^2-2*dvalue (< 2^53)\n" +
            "function am1(i,x,w,j,c,n) {\n" +
            "  while(--n >= 0) {\n" +
            "    var v = x*this[i++]+w[j]+c;\n" +
            "    c = Math.floor(v/0x4000000);\n" +
            "    w[j++] = v&0x3ffffff;\n" +
            "  }\n" +
            "  return c;\n" +
            "}\n" +
            "// am2 avoids a big mult-and-extract completely.\n" +
            "// Max digit bits should be <= 30 because we do bitwise ops\n" +
            "// on values up to 2*hdvalue^2-hdvalue-1 (< 2^31)\n" +
            "function am2(i,x,w,j,c,n) {\n" +
            "  var xl = x&0x7fff, xh = x>>15;\n" +
            "  while(--n >= 0) {\n" +
            "    var l = this[i]&0x7fff;\n" +
            "    var h = this[i++]>>15;\n" +
            "    var m = xh*l+h*xl;\n" +
            "    l = xl*l+((m&0x7fff)<<15)+w[j]+(c&0x3fffffff);\n" +
            "    c = (l>>>30)+(m>>>15)+xh*h+(c>>>30);\n" +
            "    w[j++] = l&0x3fffffff;\n" +
            "  }\n" +
            "  return c;\n" +
            "}\n" +
            "// Alternately, set max digit bits to 28 since some\n" +
            "// browsers slow down when dealing with 32-bit numbers.\n" +
            "function am3(i,x,w,j,c,n) {\n" +
            "  var xl = x&0x3fff, xh = x>>14;\n" +
            "  while(--n >= 0) {\n" +
            "    var l = this[i]&0x3fff;\n" +
            "    var h = this[i++]>>14;\n" +
            "    var m = xh*l+h*xl;\n" +
            "    l = xl*l+((m&0x3fff)<<14)+w[j]+c;\n" +
            "    c = (l>>28)+(m>>14)+xh*h;\n" +
            "    w[j++] = l&0xfffffff;\n" +
            "  }\n" +
            "  return c;\n" +
            "}\n" +
            "\n" +
            "var navigator = \"Netscape\";\n" +
            "\n" +
            "if(j_lm && (navigator == \"Microsoft Internet Explorer\")) {\n" +
            "  BigInteger.prototype.am = am2;\n" +
            "  dbits = 30;\n" +
            "}\n" +
            "else if(j_lm && (navigator != \"Netscape\")) {\n" +
            "  BigInteger.prototype.am = am1;\n" +
            "  dbits = 26;\n" +
            "}\n" +
            "else { // Mozilla/Netscape seems to prefer am3\n" +
            "  BigInteger.prototype.am = am3;\n" +
            "  dbits = 28;\n" +
            "}\n" +
            "\n" +
            "BigInteger.prototype.DB = dbits;\n" +
            "BigInteger.prototype.DM = ((1<<dbits)-1);\n" +
            "BigInteger.prototype.DV = (1<<dbits);\n" +
            "\n" +
            "var BI_FP = 52;\n" +
            "BigInteger.prototype.FV = Math.pow(2,BI_FP);\n" +
            "BigInteger.prototype.F1 = BI_FP-dbits;\n" +
            "BigInteger.prototype.F2 = 2*dbits-BI_FP;\n" +
            "\n" +
            "// Digit conversions\n" +
            "var BI_RM = \"0123456789abcdefghijklmnopqrstuvwxyz\";\n" +
            "var BI_RC = new Array();\n" +
            "var rr,vv;\n" +
            "rr = \"0\".charCodeAt(0);\n" +
            "for(vv = 0; vv <= 9; ++vv) BI_RC[rr++] = vv;\n" +
            "rr = \"a\".charCodeAt(0);\n" +
            "for(vv = 10; vv < 36; ++vv) BI_RC[rr++] = vv;\n" +
            "rr = \"A\".charCodeAt(0);\n" +
            "for(vv = 10; vv < 36; ++vv) BI_RC[rr++] = vv;\n" +
            "\n" +
            "function int2char(n) { return BI_RM.charAt(n); }\n" +
            "function intAt(s,i) {\n" +
            "  var c = BI_RC[s.charCodeAt(i)];\n" +
            "  return (c==null)?-1:c;\n" +
            "}\n" +
            "\n" +
            "// (protected) copy this to r\n" +
            "function bnpCopyTo(r) {\n" +
            "  for(var i = this.t-1; i >= 0; --i) r[i] = this[i];\n" +
            "  r.t = this.t;\n" +
            "  r.s = this.s;\n" +
            "}\n" +
            "\n" +
            "// (protected) set from integer value x, -DV <= x < DV\n" +
            "function bnpFromInt(x) {\n" +
            "  this.t = 1;\n" +
            "  this.s = (x<0)?-1:0;\n" +
            "  if(x > 0) this[0] = x;\n" +
            "  else if(x < -1) this[0] = x+DV;\n" +
            "  else this.t = 0;\n" +
            "}\n" +
            "\n" +
            "// return bigint initialized to value\n" +
            "function nbv(i) { var r = nbi(); r.fromInt(i); return r; }\n" +
            "\n" +
            "// (protected) set from string and radix\n" +
            "function bnpFromString(s,b) {\n" +
            "  var k;\n" +
            "  if(b == 16) k = 4;\n" +
            "  else if(b == 8) k = 3;\n" +
            "  else if(b == 256) k = 8; // byte array\n" +
            "  else if(b == 2) k = 1;\n" +
            "  else if(b == 32) k = 5;\n" +
            "  else if(b == 4) k = 2;\n" +
            "  else { this.fromRadix(s,b); return; }\n" +
            "  this.t = 0;\n" +
            "  this.s = 0;\n" +
            "  var i = s.length, mi = false, sh = 0;\n" +
            "  while(--i >= 0) {\n" +
            "    var x = (k==8)?s[i]&0xff:intAt(s,i);\n" +
            "    if(x < 0) {\n" +
            "      if(s.charAt(i) == \"-\") mi = true;\n" +
            "      continue;\n" +
            "    }\n" +
            "    mi = false;\n" +
            "    if(sh == 0)\n" +
            "      this[this.t++] = x;\n" +
            "    else if(sh+k > this.DB) {\n" +
            "      this[this.t-1] |= (x&((1<<(this.DB-sh))-1))<<sh;\n" +
            "      this[this.t++] = (x>>(this.DB-sh));\n" +
            "    }\n" +
            "    else\n" +
            "      this[this.t-1] |= x<<sh;\n" +
            "    sh += k;\n" +
            "    if(sh >= this.DB) sh -= this.DB;\n" +
            "  }\n" +
            "  if(k == 8 && (s[0]&0x80) != 0) {\n" +
            "    this.s = -1;\n" +
            "    if(sh > 0) this[this.t-1] |= ((1<<(this.DB-sh))-1)<<sh;\n" +
            "  }\n" +
            "  this.clamp();\n" +
            "  if(mi) BigInteger.ZERO.subTo(this,this);\n" +
            "}\n" +
            "\n" +
            "// (protected) clamp off excess high words\n" +
            "function bnpClamp() {\n" +
            "  var c = this.s&this.DM;\n" +
            "  while(this.t > 0 && this[this.t-1] == c) --this.t;\n" +
            "}\n" +
            "\n" +
            "// (public) return string representation in given radix\n" +
            "function bnToString(b) {\n" +
            "  if(this.s < 0) return \"-\"+this.negate().toString(b);\n" +
            "  var k;\n" +
            "  if(b == 16) k = 4;\n" +
            "  else if(b == 8) k = 3;\n" +
            "  else if(b == 2) k = 1;\n" +
            "  else if(b == 32) k = 5;\n" +
            "  else if(b == 4) k = 2;\n" +
            "  else return this.toRadix(b);\n" +
            "  var km = (1<<k)-1, d, m = false, r = \"\", i = this.t;\n" +
            "  var p = this.DB-(i*this.DB)%k;\n" +
            "  if(i-- > 0) {\n" +
            "    if(p < this.DB && (d = this[i]>>p) > 0) { m = true; r = int2char(d); }\n" +
            "    while(i >= 0) {\n" +
            "      if(p < k) {\n" +
            "        d = (this[i]&((1<<p)-1))<<(k-p);\n" +
            "        d |= this[--i]>>(p+=this.DB-k);\n" +
            "      }\n" +
            "      else {\n" +
            "        d = (this[i]>>(p-=k))&km;\n" +
            "        if(p <= 0) { p += this.DB; --i; }\n" +
            "      }\n" +
            "      if(d > 0) m = true;\n" +
            "      if(m) r += int2char(d);\n" +
            "    }\n" +
            "  }\n" +
            "  return m?r:\"0\";\n" +
            "}\n" +
            "\n" +
            "// (public) -this\n" +
            "function bnNegate() { var r = nbi(); BigInteger.ZERO.subTo(this,r); return r; }\n" +
            "\n" +
            "// (public) |this|\n" +
            "function bnAbs() { return (this.s<0)?this.negate():this; }\n" +
            "\n" +
            "// (public) return + if this > a, - if this < a, 0 if equal\n" +
            "function bnCompareTo(a) {\n" +
            "  var r = this.s-a.s;\n" +
            "  if(r != 0) return r;\n" +
            "  var i = this.t;\n" +
            "  r = i-a.t;\n" +
            "  if(r != 0) return (this.s<0)?-r:r;\n" +
            "  while(--i >= 0) if((r=this[i]-a[i]) != 0) return r;\n" +
            "  return 0;\n" +
            "}\n" +
            "\n" +
            "// returns bit length of the integer x\n" +
            "function nbits(x) {\n" +
            "  var r = 1, t;\n" +
            "  if((t=x>>>16) != 0) { x = t; r += 16; }\n" +
            "  if((t=x>>8) != 0) { x = t; r += 8; }\n" +
            "  if((t=x>>4) != 0) { x = t; r += 4; }\n" +
            "  if((t=x>>2) != 0) { x = t; r += 2; }\n" +
            "  if((t=x>>1) != 0) { x = t; r += 1; }\n" +
            "  return r;\n" +
            "}\n" +
            "\n" +
            "// (public) return the number of bits in \"this\"\n" +
            "function bnBitLength() {\n" +
            "  if(this.t <= 0) return 0;\n" +
            "  return this.DB*(this.t-1)+nbits(this[this.t-1]^(this.s&this.DM));\n" +
            "}\n" +
            "\n" +
            "// (protected) r = this << n*DB\n" +
            "function bnpDLShiftTo(n,r) {\n" +
            "  var i;\n" +
            "  for(i = this.t-1; i >= 0; --i) r[i+n] = this[i];\n" +
            "  for(i = n-1; i >= 0; --i) r[i] = 0;\n" +
            "  r.t = this.t+n;\n" +
            "  r.s = this.s;\n" +
            "}\n" +
            "\n" +
            "// (protected) r = this >> n*DB\n" +
            "function bnpDRShiftTo(n,r) {\n" +
            "  for(var i = n; i < this.t; ++i) r[i-n] = this[i];\n" +
            "  r.t = Math.max(this.t-n,0);\n" +
            "  r.s = this.s;\n" +
            "}\n" +
            "\n" +
            "// (protected) r = this << n\n" +
            "function bnpLShiftTo(n,r) {\n" +
            "  var bs = n%this.DB;\n" +
            "  var cbs = this.DB-bs;\n" +
            "  var bm = (1<<cbs)-1;\n" +
            "  var ds = Math.floor(n/this.DB), c = (this.s<<bs)&this.DM, i;\n" +
            "  for(i = this.t-1; i >= 0; --i) {\n" +
            "    r[i+ds+1] = (this[i]>>cbs)|c;\n" +
            "    c = (this[i]&bm)<<bs;\n" +
            "  }\n" +
            "  for(i = ds-1; i >= 0; --i) r[i] = 0;\n" +
            "  r[ds] = c;\n" +
            "  r.t = this.t+ds+1;\n" +
            "  r.s = this.s;\n" +
            "  r.clamp();\n" +
            "}\n" +
            "\n" +
            "// (protected) r = this >> n\n" +
            "function bnpRShiftTo(n,r) {\n" +
            "  r.s = this.s;\n" +
            "  var ds = Math.floor(n/this.DB);\n" +
            "  if(ds >= this.t) { r.t = 0; return; }\n" +
            "  var bs = n%this.DB;\n" +
            "  var cbs = this.DB-bs;\n" +
            "  var bm = (1<<bs)-1;\n" +
            "  r[0] = this[ds]>>bs;\n" +
            "  for(var i = ds+1; i < this.t; ++i) {\n" +
            "    r[i-ds-1] |= (this[i]&bm)<<cbs;\n" +
            "    r[i-ds] = this[i]>>bs;\n" +
            "  }\n" +
            "  if(bs > 0) r[this.t-ds-1] |= (this.s&bm)<<cbs;\n" +
            "  r.t = this.t-ds;\n" +
            "  r.clamp();\n" +
            "}\n" +
            "\n" +
            "// (protected) r = this - a\n" +
            "function bnpSubTo(a,r) {\n" +
            "  var i = 0, c = 0, m = Math.min(a.t,this.t);\n" +
            "  while(i < m) {\n" +
            "    c += this[i]-a[i];\n" +
            "    r[i++] = c&this.DM;\n" +
            "    c >>= this.DB;\n" +
            "  }\n" +
            "  if(a.t < this.t) {\n" +
            "    c -= a.s;\n" +
            "    while(i < this.t) {\n" +
            "      c += this[i];\n" +
            "      r[i++] = c&this.DM;\n" +
            "      c >>= this.DB;\n" +
            "    }\n" +
            "    c += this.s;\n" +
            "  }\n" +
            "  else {\n" +
            "    c += this.s;\n" +
            "    while(i < a.t) {\n" +
            "      c -= a[i];\n" +
            "      r[i++] = c&this.DM;\n" +
            "      c >>= this.DB;\n" +
            "    }\n" +
            "    c -= a.s;\n" +
            "  }\n" +
            "  r.s = (c<0)?-1:0;\n" +
            "  if(c < -1) r[i++] = this.DV+c;\n" +
            "  else if(c > 0) r[i++] = c;\n" +
            "  r.t = i;\n" +
            "  r.clamp();\n" +
            "}\n" +
            "\n" +
            "// (protected) r = this * a, r != this,a (HAC 14.12)\n" +
            "// \"this\" should be the larger one if appropriate.\n" +
            "function bnpMultiplyTo(a,r) {\n" +
            "  var x = this.abs(), y = a.abs();\n" +
            "  var i = x.t;\n" +
            "  r.t = i+y.t;\n" +
            "  while(--i >= 0) r[i] = 0;\n" +
            "  for(i = 0; i < y.t; ++i) r[i+x.t] = x.am(0,y[i],r,i,0,x.t);\n" +
            "  r.s = 0;\n" +
            "  r.clamp();\n" +
            "  if(this.s != a.s) BigInteger.ZERO.subTo(r,r);\n" +
            "}\n" +
            "\n" +
            "// (protected) r = this^2, r != this (HAC 14.16)\n" +
            "function bnpSquareTo(r) {\n" +
            "  var x = this.abs();\n" +
            "  var i = r.t = 2*x.t;\n" +
            "  while(--i >= 0) r[i] = 0;\n" +
            "  for(i = 0; i < x.t-1; ++i) {\n" +
            "    var c = x.am(i,x[i],r,2*i,0,1);\n" +
            "    if((r[i+x.t]+=x.am(i+1,2*x[i],r,2*i+1,c,x.t-i-1)) >= x.DV) {\n" +
            "      r[i+x.t] -= x.DV;\n" +
            "      r[i+x.t+1] = 1;\n" +
            "    }\n" +
            "  }\n" +
            "  if(r.t > 0) r[r.t-1] += x.am(i,x[i],r,2*i,0,1);\n" +
            "  r.s = 0;\n" +
            "  r.clamp();\n" +
            "}\n" +
            "\n" +
            "// (protected) divide this by m, quotient and remainder to q, r (HAC 14.20)\n" +
            "// r != q, this != m.  q or r may be null.\n" +
            "function bnpDivRemTo(m,q,r) {\n" +
            "  var pm = m.abs();\n" +
            "  if(pm.t <= 0) return;\n" +
            "  var pt = this.abs();\n" +
            "  if(pt.t < pm.t) {\n" +
            "    if(q != null) q.fromInt(0);\n" +
            "    if(r != null) this.copyTo(r);\n" +
            "    return;\n" +
            "  }\n" +
            "  if(r == null) r = nbi();\n" +
            "  var y = nbi(), ts = this.s, ms = m.s;\n" +
            "  var nsh = this.DB-nbits(pm[pm.t-1]);\t// normalize modulus\n" +
            "  if(nsh > 0) { pm.lShiftTo(nsh,y); pt.lShiftTo(nsh,r); }\n" +
            "  else { pm.copyTo(y); pt.copyTo(r); }\n" +
            "  var ys = y.t;\n" +
            "  var y0 = y[ys-1];\n" +
            "  if(y0 == 0) return;\n" +
            "  var yt = y0*(1<<this.F1)+((ys>1)?y[ys-2]>>this.F2:0);\n" +
            "  var d1 = this.FV/yt, d2 = (1<<this.F1)/yt, e = 1<<this.F2;\n" +
            "  var i = r.t, j = i-ys, t = (q==null)?nbi():q;\n" +
            "  y.dlShiftTo(j,t);\n" +
            "  if(r.compareTo(t) >= 0) {\n" +
            "    r[r.t++] = 1;\n" +
            "    r.subTo(t,r);\n" +
            "  }\n" +
            "  BigInteger.ONE.dlShiftTo(ys,t);\n" +
            "  t.subTo(y,y);\t// \"negative\" y so we can replace sub with am later\n" +
            "  while(y.t < ys) y[y.t++] = 0;\n" +
            "  while(--j >= 0) {\n" +
            "    // Estimate quotient digit\n" +
            "    var qd = (r[--i]==y0)?this.DM:Math.floor(r[i]*d1+(r[i-1]+e)*d2);\n" +
            "    if((r[i]+=y.am(0,qd,r,j,0,ys)) < qd) {\t// Try it out\n" +
            "      y.dlShiftTo(j,t);\n" +
            "      r.subTo(t,r);\n" +
            "      while(r[i] < --qd) r.subTo(t,r);\n" +
            "    }\n" +
            "  }\n" +
            "  if(q != null) {\n" +
            "    r.drShiftTo(ys,q);\n" +
            "    if(ts != ms) BigInteger.ZERO.subTo(q,q);\n" +
            "  }\n" +
            "  r.t = ys;\n" +
            "  r.clamp();\n" +
            "  if(nsh > 0) r.rShiftTo(nsh,r);\t// Denormalize remainder\n" +
            "  if(ts < 0) BigInteger.ZERO.subTo(r,r);\n" +
            "}\n" +
            "\n" +
            "// (public) this mod a\n" +
            "function bnMod(a) {\n" +
            "  var r = nbi();\n" +
            "  this.abs().divRemTo(a,null,r);\n" +
            "  if(this.s < 0 && r.compareTo(BigInteger.ZERO) > 0) a.subTo(r,r);\n" +
            "  return r;\n" +
            "}\n" +
            "\n" +
            "// Modular reduction using \"classic\" algorithm\n" +
            "function Classic(m) { this.m = m; }\n" +
            "function cConvert(x) {\n" +
            "  if(x.s < 0 || x.compareTo(this.m) >= 0) return x.mod(this.m);\n" +
            "  else return x;\n" +
            "}\n" +
            "function cRevert(x) { return x; }\n" +
            "function cReduce(x) { x.divRemTo(this.m,null,x); }\n" +
            "function cMulTo(x,y,r) { x.multiplyTo(y,r); this.reduce(r); }\n" +
            "function cSqrTo(x,r) { x.squareTo(r); this.reduce(r); }\n" +
            "\n" +
            "Classic.prototype.convert = cConvert;\n" +
            "Classic.prototype.revert = cRevert;\n" +
            "Classic.prototype.reduce = cReduce;\n" +
            "Classic.prototype.mulTo = cMulTo;\n" +
            "Classic.prototype.sqrTo = cSqrTo;\n" +
            "\n" +
            "// (protected) return \"-1/this % 2^DB\"; useful for Mont. reduction\n" +
            "// justification:\n" +
            "//         xy == 1 (mod m)\n" +
            "//         xy =  1+km\n" +
            "//   xy(2-xy) = (1+km)(1-km)\n" +
            "// x[y(2-xy)] = 1-k^2m^2\n" +
            "// x[y(2-xy)] == 1 (mod m^2)\n" +
            "// if y is 1/x mod m, then y(2-xy) is 1/x mod m^2\n" +
            "// should reduce x and y(2-xy) by m^2 at each step to keep size bounded.\n" +
            "// JS multiply \"overflows\" differently from C/C++, so care is needed here.\n" +
            "function bnpInvDigit() {\n" +
            "  if(this.t < 1) return 0;\n" +
            "  var x = this[0];\n" +
            "  if((x&1) == 0) return 0;\n" +
            "  var y = x&3;\t\t// y == 1/x mod 2^2\n" +
            "  y = (y*(2-(x&0xf)*y))&0xf;\t// y == 1/x mod 2^4\n" +
            "  y = (y*(2-(x&0xff)*y))&0xff;\t// y == 1/x mod 2^8\n" +
            "  y = (y*(2-(((x&0xffff)*y)&0xffff)))&0xffff;\t// y == 1/x mod 2^16\n" +
            "  // last step - calculate inverse mod DV directly;\n" +
            "  // assumes 16 < DB <= 32 and assumes ability to handle 48-bit ints\n" +
            "  y = (y*(2-x*y%this.DV))%this.DV;\t\t// y == 1/x mod 2^dbits\n" +
            "  // we really want the negative inverse, and -DV < y < DV\n" +
            "  return (y>0)?this.DV-y:-y;\n" +
            "}\n" +
            "\n" +
            "// Montgomery reduction\n" +
            "function Montgomery(m) {\n" +
            "  this.m = m;\n" +
            "  this.mp = m.invDigit();\n" +
            "  this.mpl = this.mp&0x7fff;\n" +
            "  this.mph = this.mp>>15;\n" +
            "  this.um = (1<<(m.DB-15))-1;\n" +
            "  this.mt2 = 2*m.t;\n" +
            "}\n" +
            "\n" +
            "// xR mod m\n" +
            "function montConvert(x) {\n" +
            "  var r = nbi();\n" +
            "  x.abs().dlShiftTo(this.m.t,r);\n" +
            "  r.divRemTo(this.m,null,r);\n" +
            "  if(x.s < 0 && r.compareTo(BigInteger.ZERO) > 0) this.m.subTo(r,r);\n" +
            "  return r;\n" +
            "}\n" +
            "\n" +
            "// x/R mod m\n" +
            "function montRevert(x) {\n" +
            "  var r = nbi();\n" +
            "  x.copyTo(r);\n" +
            "  this.reduce(r);\n" +
            "  return r;\n" +
            "}\n" +
            "\n" +
            "// x = x/R mod m (HAC 14.32)\n" +
            "function montReduce(x) {\n" +
            "  while(x.t <= this.mt2)\t// pad x so am has enough room later\n" +
            "    x[x.t++] = 0;\n" +
            "  for(var i = 0; i < this.m.t; ++i) {\n" +
            "    // faster way of calculating u0 = x[i]*mp mod DV\n" +
            "    var j = x[i]&0x7fff;\n" +
            "    var u0 = (j*this.mpl+(((j*this.mph+(x[i]>>15)*this.mpl)&this.um)<<15))&x.DM;\n" +
            "    // use am to combine the multiply-shift-add into one call\n" +
            "    j = i+this.m.t;\n" +
            "    x[j] += this.m.am(0,u0,x,i,0,this.m.t);\n" +
            "    // propagate carry\n" +
            "    while(x[j] >= x.DV) { x[j] -= x.DV; x[++j]++; }\n" +
            "  }\n" +
            "  x.clamp();\n" +
            "  x.drShiftTo(this.m.t,x);\n" +
            "  if(x.compareTo(this.m) >= 0) x.subTo(this.m,x);\n" +
            "}\n" +
            "\n" +
            "// r = \"x^2/R mod m\"; x != r\n" +
            "function montSqrTo(x,r) { x.squareTo(r); this.reduce(r); }\n" +
            "\n" +
            "// r = \"xy/R mod m\"; x,y != r\n" +
            "function montMulTo(x,y,r) { x.multiplyTo(y,r); this.reduce(r); }\n" +
            "\n" +
            "Montgomery.prototype.convert = montConvert;\n" +
            "Montgomery.prototype.revert = montRevert;\n" +
            "Montgomery.prototype.reduce = montReduce;\n" +
            "Montgomery.prototype.mulTo = montMulTo;\n" +
            "Montgomery.prototype.sqrTo = montSqrTo;\n" +
            "\n" +
            "// (protected) true iff this is even\n" +
            "function bnpIsEven() { return ((this.t>0)?(this[0]&1):this.s) == 0; }\n" +
            "\n" +
            "// (protected) this^e, e < 2^32, doing sqr and mul with \"r\" (HAC 14.79)\n" +
            "function bnpExp(e,z) {\n" +
            "  if(e > 0xffffffff || e < 1) return BigInteger.ONE;\n" +
            "  var r = nbi(), r2 = nbi(), g = z.convert(this), i = nbits(e)-1;\n" +
            "  g.copyTo(r);\n" +
            "  while(--i >= 0) {\n" +
            "    z.sqrTo(r,r2);\n" +
            "    if((e&(1<<i)) > 0) z.mulTo(r2,g,r);\n" +
            "    else { var t = r; r = r2; r2 = t; }\n" +
            "  }\n" +
            "  return z.revert(r);\n" +
            "}\n" +
            "\n" +
            "// (public) this^e % m, 0 <= e < 2^32\n" +
            "function bnModPowInt(e,m) {\n" +
            "  var z;\n" +
            "  if(e < 256 || m.isEven()) z = new Classic(m); else z = new Montgomery(m);\n" +
            "  return this.exp(e,z);\n" +
            "}\n" +
            "\n" +
            "// protected\n" +
            "BigInteger.prototype.copyTo = bnpCopyTo;\n" +
            "BigInteger.prototype.fromInt = bnpFromInt;\n" +
            "BigInteger.prototype.fromString = bnpFromString;\n" +
            "BigInteger.prototype.clamp = bnpClamp;\n" +
            "BigInteger.prototype.dlShiftTo = bnpDLShiftTo;\n" +
            "BigInteger.prototype.drShiftTo = bnpDRShiftTo;\n" +
            "BigInteger.prototype.lShiftTo = bnpLShiftTo;\n" +
            "BigInteger.prototype.rShiftTo = bnpRShiftTo;\n" +
            "BigInteger.prototype.subTo = bnpSubTo;\n" +
            "BigInteger.prototype.multiplyTo = bnpMultiplyTo;\n" +
            "BigInteger.prototype.squareTo = bnpSquareTo;\n" +
            "BigInteger.prototype.divRemTo = bnpDivRemTo;\n" +
            "BigInteger.prototype.invDigit = bnpInvDigit;\n" +
            "BigInteger.prototype.isEven = bnpIsEven;\n" +
            "BigInteger.prototype.exp = bnpExp;\n" +
            "\n" +
            "// public\n" +
            "BigInteger.prototype.toString = bnToString;\n" +
            "BigInteger.prototype.negate = bnNegate;\n" +
            "BigInteger.prototype.abs = bnAbs;\n" +
            "BigInteger.prototype.compareTo = bnCompareTo;\n" +
            "BigInteger.prototype.bitLength = bnBitLength;\n" +
            "BigInteger.prototype.mod = bnMod;\n" +
            "BigInteger.prototype.modPowInt = bnModPowInt;\n" +
            "\n" +
            "// \"constants\"\n" +
            "BigInteger.ZERO = nbv(0);\n" +
            "BigInteger.ONE = nbv(1);\n" +
            "// prng4.js - uses Arcfour as a PRNG\n" +
            "\n" +
            "function Arcfour() {\n" +
            "  this.i = 0;\n" +
            "  this.j = 0;\n" +
            "  this.S = new Array();\n" +
            "}\n" +
            "\n" +
            "// Initialize arcfour context from key, an array of ints, each from [0..255]\n" +
            "function ARC4init(key) {\n" +
            "  var i, j, t;\n" +
            "  for(i = 0; i < 256; ++i)\n" +
            "    this.S[i] = i;\n" +
            "  j = 0;\n" +
            "  for(i = 0; i < 256; ++i) {\n" +
            "    j = (j + this.S[i] + key[i % key.length]) & 255;\n" +
            "    t = this.S[i];\n" +
            "    this.S[i] = this.S[j];\n" +
            "    this.S[j] = t;\n" +
            "  }\n" +
            "  this.i = 0;\n" +
            "  this.j = 0;\n" +
            "}\n" +
            "\n" +
            "function ARC4next() {\n" +
            "  var t;\n" +
            "  this.i = (this.i + 1) & 255;\n" +
            "  this.j = (this.j + this.S[this.i]) & 255;\n" +
            "  t = this.S[this.i];\n" +
            "  this.S[this.i] = this.S[this.j];\n" +
            "  this.S[this.j] = t;\n" +
            "  return this.S[(t + this.S[this.i]) & 255];\n" +
            "}\n" +
            "\n" +
            "Arcfour.prototype.init = ARC4init;\n" +
            "Arcfour.prototype.next = ARC4next;\n" +
            "\n" +
            "// Plug in your RNG constructor here\n" +
            "function prng_newstate() {\n" +
            "  return new Arcfour();\n" +
            "}\n" +
            "\n" +
            "// Pool size must be a multiple of 4 and greater than 32.\n" +
            "// An array of bytes the size of the pool will be passed to init()\n" +
            "var rng_psize = 256;\n" +
            "// Random number generator - requires a PRNG backend, e.g. prng4.js\n" +
            "\n" +
            "// For best results, put code like\n" +
            "// <body onClick='rng_seed_time();' onKeyPress='rng_seed_time();'>\n" +
            "// in your main HTML document.\n" +
            "\n" +
            "var rng_state;\n" +
            "var rng_pool;\n" +
            "var rng_pptr;\n" +
            "\n" +
            "// Mix in a 32-bit integer into the pool\n" +
            "function rng_seed_int(x) {\n" +
            "  rng_pool[rng_pptr++] ^= x & 255;\n" +
            "  rng_pool[rng_pptr++] ^= (x >> 8) & 255;\n" +
            "  rng_pool[rng_pptr++] ^= (x >> 16) & 255;\n" +
            "  rng_pool[rng_pptr++] ^= (x >> 24) & 255;\n" +
            "  if(rng_pptr >= rng_psize) rng_pptr -= rng_psize;\n" +
            "}\n" +
            "\n" +
            "// Mix in the current time (w/milliseconds) into the pool\n" +
            "function rng_seed_time() {\n" +
            "  rng_seed_int(new Date().getTime());\n" +
            "}\n" +
            "\n" +
            "// Initialize the pool with junk if needed.\n" +
            "if(rng_pool == null) {\n" +
            "  rng_pool = new Array();\n" +
            "  rng_pptr = 0;\n" +
            "  var t;\n" +
            "  if(navigator.appName == \"Netscape\" && navigator.appVersion < \"5\" && window.crypto) {\n" +
            "    // Extract entropy (256 bits) from NS4 RNG if available\n" +
            "    var z = window.crypto.random(32);\n" +
            "    for(t = 0; t < z.length; ++t)\n" +
            "      rng_pool[rng_pptr++] = z.charCodeAt(t) & 255;\n" +
            "  }\n" +
            "  while(rng_pptr < rng_psize) {  // extract some randomness from Math.random()\n" +
            "    t = Math.floor(65536 * Math.random());\n" +
            "    rng_pool[rng_pptr++] = t >>> 8;\n" +
            "    rng_pool[rng_pptr++] = t & 255;\n" +
            "  }\n" +
            "  rng_pptr = 0;\n" +
            "  rng_seed_time();\n" +
            "  //rng_seed_int(window.screenX);\n" +
            "  //rng_seed_int(window.screenY);\n" +
            "}\n" +
            "\n" +
            "function rng_get_byte() {\n" +
            "  if(rng_state == null) {\n" +
            "    rng_seed_time();\n" +
            "    rng_state = prng_newstate();\n" +
            "    rng_state.init(rng_pool);\n" +
            "    for(rng_pptr = 0; rng_pptr < rng_pool.length; ++rng_pptr)\n" +
            "      rng_pool[rng_pptr] = 0;\n" +
            "    rng_pptr = 0;\n" +
            "    //rng_pool = null;\n" +
            "  }\n" +
            "  // TODO: allow reseeding after first request\n" +
            "  return rng_state.next();\n" +
            "}\n" +
            "\n" +
            "function rng_get_bytes(ba) {\n" +
            "  var i;\n" +
            "  for(i = 0; i < ba.length; ++i) ba[i] = rng_get_byte();\n" +
            "}\n" +
            "\n" +
            "function SecureRandom() {}\n" +
            "\n" +
            "SecureRandom.prototype.nextBytes = rng_get_bytes;\n" +
            "// Depends on jsbn.js and rng.js\n" +
            "\n" +
            "// Version 1.1: support utf-8 encoding in pkcs1pad2\n" +
            "\n" +
            "// convert a (hex) string to a bignum object\n" +
            "function parseBigInt(str,r) {\n" +
            "  return new BigInteger(str,r);\n" +
            "}\n" +
            "\n" +
            "function linebrk(s,n) {\n" +
            "  var ret = \"\";\n" +
            "  var i = 0;\n" +
            "  while(i + n < s.length) {\n" +
            "    ret += s.substring(i,i+n) + \"\\n\";\n" +
            "    i += n;\n" +
            "  }\n" +
            "  return ret + s.substring(i,s.length);\n" +
            "}\n" +
            "\n" +
            "function byte2Hex(b) {\n" +
            "  if(b < 0x10)\n" +
            "    return \"0\" + b.toString(16);\n" +
            "  else\n" +
            "    return b.toString(16);\n" +
            "}\n" +
            "\n" +
            "// PKCS#1 (type 2, random) pad input string s to n bytes, and return a bigint\n" +
            "function pkcs1pad2(s,n) {\n" +
            "  if(n < s.length + 11) { // TODO: fix for utf-8\n" +
            "    return null;\n" +
            "  }\n" +
            "  var ba = new Array();\n" +
            "  var i = s.length - 1;\n" +
            "  while(i >= 0 && n > 0) {\n" +
            "    var c = s.charCodeAt(i--);\n" +
            "    if(c < 128) { // encode using utf-8\n" +
            "      ba[--n] = c;\n" +
            "    }\n" +
            "    else if((c > 127) && (c < 2048)) {\n" +
            "      ba[--n] = (c & 63) | 128;\n" +
            "      ba[--n] = (c >> 6) | 192;\n" +
            "    }\n" +
            "    else {\n" +
            "      ba[--n] = (c & 63) | 128;\n" +
            "      ba[--n] = ((c >> 6) & 63) | 128;\n" +
            "      ba[--n] = (c >> 12) | 224;\n" +
            "    }\n" +
            "  }\n" +
            "  ba[--n] = 0;\n" +
            "  var rng = new SecureRandom();\n" +
            "  var x = new Array();\n" +
            "  while(n > 2) { // random non-zero pad\n" +
            "    x[0] = 0;\n" +
            "    while(x[0] == 0) rng.nextBytes(x);\n" +
            "    ba[--n] = x[0];\n" +
            "  }\n" +
            "  ba[--n] = 2;\n" +
            "  ba[--n] = 0;\n" +
            "  return new BigInteger(ba);\n" +
            "}\n" +
            "\n" +
            "// \"empty\" RSA key constructor\n" +
            "function RSAKey() {\n" +
            "  this.n = null;\n" +
            "  this.e = 0;\n" +
            "  this.d = null;\n" +
            "  this.p = null;\n" +
            "  this.q = null;\n" +
            "  this.dmp1 = null;\n" +
            "  this.dmq1 = null;\n" +
            "  this.coeff = null;\n" +
            "}\n" +
            "\n" +
            "// Set the public key fields N and e from hex strings\n" +
            "function RSASetPublic(N,E) {\n" +
            "  if(N != null && E != null && N.length > 0 && E.length > 0) {\n" +
            "    this.n = parseBigInt(N,16);\n" +
            "    this.e = parseInt(E,16);\n" +
            "  }\n" +
            "}\n" +
            "\n" +
            "// Perform raw public operation on \"x\": return x^e (mod n)\n" +
            "function RSADoPublic(x) {\n" +
            "  return x.modPowInt(this.e, this.n);\n" +
            "}\n" +
            "\n" +
            "// Return the PKCS#1 RSA encryption of \"text\" as an even-length hex string\n" +
            "function RSAEncrypt(text) {\n" +
            "  var m = pkcs1pad2(text,(this.n.bitLength()+7)>>3);\n" +
            "  if(m == null) return null;\n" +
            "  var c = this.doPublic(m);\n" +
            "  if(c == null) return null;\n" +
            "  var h = c.toString(16);\n" +
            "  if((h.length & 1) == 0) return h; else return \"0\" + h;\n" +
            "}\n" +
            "\n" +
            "// Return the PKCS#1 RSA encryption of \"text\" as a Base64-encoded string\n" +
            "function RSAEncryptB64(text) {\n" +
            "  var h = this.encrypt(text);\n" +
            "}\n" +
            "\n" +
            "// protected\n" +
            "RSAKey.prototype.doPublic = RSADoPublic;\n" +
            "\n" +
            "// public\n" +
            "RSAKey.prototype.setPublic = RSASetPublic;\n" +
            "RSAKey.prototype.encrypt = RSAEncrypt;\n" +
            "RSAKey.prototype.encrypt_b64 = RSAEncryptB64;\n" +
            "\n" +
            "\n" +
            "function MyRSAEncryptB64(text,modulus,publicExponent){\n" +
            "\n" +
            "var rsa = new RSAKey();\n" +
            "rsa.setPublic(modulus, publicExponent);\n" +
            "var res = rsa.encrypt(text);\n" +
            "return res;\n" +
            "}\n" +
            "";

    public static String getRandomPassword() {

        String allChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890@.!-";
        StringBuilder pass = new StringBuilder();
        Random rnd = new Random();
        while (pass.length() < 8) {
            int index = (int) (rnd.nextFloat() * allChars.length());
            pass.append(allChars.charAt(index));
        }
        return pass.toString();
    }

    public static String sha256Encode(String s) {
        try {
            engine.eval(SHA256FUNC);
        } catch (ScriptException e) {
            return null;
        }

        Invocable inv = (Invocable) engine;
        // invoke the global function named "BASE64ENCODE"

        try {
            String encode = (String) inv.invokeFunction("SHA256", s);
            return encode;
        } catch (Exception e) {
            return null;
        }
    }

    public static String base64Encode(String s) {
        try {
            engine.eval(BASE64ENCODEFUNC);
        } catch (ScriptException e) {
            return null;
        }

        Invocable inv = (Invocable) engine;
        // invoke the global function named "base64Encode"

        try {
            String encode = (String) inv.invokeFunction("base64encode", s, base64EncodeChars);
            return encode;
        } catch (Exception e) {
            return null;
        }
    }

    public static String hexMd5(String s) {
        try {
            engine.eval(HEXMD5FUNC);
        } catch (ScriptException e) {
            return null;
        }

        Invocable inv = (Invocable) engine;
        // invoke the global function named "hexMd5"

        try {
            String encode = (String) inv.invokeFunction("hexMd5", s);
            return encode;
        } catch (Exception e) {
            return null;
        }
    }

    public static String RSAEncrypt(String text, String rasn, String rase) {

        try {
            engine.eval(RSAFUNC);
        } catch (ScriptException e) {
            e.printStackTrace();
        }

        Invocable inv = (Invocable) engine;

        String res = null;
        try {
            //rasn:modulus, rase:publicExponent
            res = (String) inv.invokeFunction("MyRSAEncryptB64", text, rasn, rase);
        } catch (ScriptException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return res;

    }

}
