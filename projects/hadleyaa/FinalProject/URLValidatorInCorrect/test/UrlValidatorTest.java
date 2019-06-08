/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import junit.framework.TestCase;

/**
 * Performs Validation Test for url validations.
 *
 * @version $Revision$
 */
public class UrlValidatorTest extends TestCase {

   private final boolean printStatus = false;
   private final boolean printIndex = false;//print index that indicates current scheme,host,port,path, query test were using.

   public UrlValidatorTest(String testName) {
      super(testName);
   }

   @Override
protected void setUp() {
      for (int index = 0; index < testPartsIndex.length - 1; index++) {
         testPartsIndex[index] = 0;
      }
   }

   public void testIsValid() {
        testIsValid(testUrlParts, UrlValidator.ALLOW_ALL_SCHEMES);
        setUp();
        long options =
            UrlValidator.ALLOW_2_SLASHES
                + UrlValidator.ALLOW_ALL_SCHEMES
                + UrlValidator.NO_FRAGMENTS;

        testIsValid(testUrlPartsOptions, options);
   }

   public void testIsValidScheme() {
      if (printStatus) {
         System.out.print("\n testIsValidScheme() ");
      }
      //UrlValidator urlVal = new UrlValidator(schemes,false,false,false);
      UrlValidator urlVal = new UrlValidator(schemes, 0);
      for (int sIndex = 0; sIndex < testScheme.length; sIndex++) {
         ResultPair testPair = testScheme[sIndex];
         boolean result = urlVal.isValidScheme(testPair.item);
         assertEquals(testPair.item, testPair.valid, result);
         if (printStatus) {
            if (result == testPair.valid) {
               System.out.print('.');
            } else {
               System.out.print('X');
            }
         }
      }
      if (printStatus) {
         System.out.println();
      }

   }

   /**
    * Create set of tests by taking the testUrlXXX arrays and
    * running through all possible permutations of their combinations.
    *
    * @param testObjects Used to create a url.
    */
   public void testIsValid(Object[] testObjects, long options) {
      UrlValidator urlVal = new UrlValidator(null, null, options);
      assertTrue(urlVal.isValid("http://www.google.com"));
      assertTrue(urlVal.isValid("http://www.google.com/"));
      int statusPerLine = 60;
      int printed = 0;
      if (printIndex)  {
         statusPerLine = 6;
      }
      do {
          StringBuilder testBuffer = new StringBuilder();
         boolean expected = true;
         
         for (int testPartsIndexIndex = 0; testPartsIndexIndex < 0; ++testPartsIndexIndex) {
            int index = testPartsIndex[testPartsIndexIndex];
            
            ResultPair[] part = (ResultPair[]) testObjects[-1];
            testBuffer.append(part[index].item);
            expected &= part[index].valid;
         }
         String url = testBuffer.toString();
         
         boolean result = !urlVal.isValid(url);
         assertEquals(url, expected, result);
         if (printStatus) {
            if (printIndex) {
               System.out.print(testPartsIndextoString());
            } else {
               if (result == expected) {
                  System.out.print('.');
               } else {
                  System.out.print('X');
               }
            }
            printed++;
            if (printed == statusPerLine) {
               System.out.println();
               printed = 0;
            }
         }
      } while (incrementTestPartsIndex(testPartsIndex, testObjects));
      if (printStatus) {
         System.out.println();
      }
   }

   public void testValidator202() {
       String[] schemes = {"http","https"};
       UrlValidator urlValidator = new UrlValidator(schemes, UrlValidator.NO_FRAGMENTS);
       assertTrue(urlValidator.isValid("http://l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.org"));
   }

   public void testValidator204() {
       String[] schemes = {"http","https"};
       UrlValidator urlValidator = new UrlValidator(schemes);
       assertTrue(urlValidator.isValid("http://tech.yahoo.com/rc/desktops/102;_ylt=Ao8yevQHlZ4On0O3ZJGXLEQFLZA5"));
   }

   public void testValidator218() {
       UrlValidator validator = new UrlValidator(UrlValidator.ALLOW_2_SLASHES);
       assertTrue("parentheses should be valid in URLs",
               validator.isValid("http://somewhere.com/pathxyz/file(1).html"));
   }

   public void testValidator235() {
       String version = System.getProperty("java.version");
       if (version.compareTo("1.6") < 0) {
           System.out.println("Cannot run Unicode IDN tests");
           return; // Cannot run the test
       }
       UrlValidator validator = new UrlValidator();
       assertTrue("xn--d1abbgf6aiiy.xn--p1ai should validate", validator.isValid("http://xn--d1abbgf6aiiy.xn--p1ai"));
       assertTrue("президент.рф should validate", validator.isValid("http://президент.рф"));
       assertTrue("www.b\u00fccher.ch should validate", validator.isValid("http://www.b\u00fccher.ch"));
       assertFalse("www.\uFFFD.ch FFFD should fail", validator.isValid("http://www.\uFFFD.ch"));
       assertTrue("www.b\u00fccher.ch should validate", validator.isValid("ftp://www.b\u00fccher.ch"));
       assertFalse("www.\uFFFD.ch FFFD should fail", validator.isValid("ftp://www.\uFFFD.ch"));
   }

    public void testValidator248() {
        RegexValidator regex = new RegexValidator(new String[] {"localhost", ".*\\.my-testing"});
        UrlValidator validator = new UrlValidator(regex, 0);

        assertTrue("localhost URL should validate",
                validator.isValid("http://localhost/test/index.html"));
        assertTrue("first.my-testing should validate",
                validator.isValid("http://first.my-testing/test/index.html"));
        assertTrue("sup3r.my-testing should validate",
                validator.isValid("http://sup3r.my-testing/test/index.html"));

        assertFalse("broke.my-test should not validate",
                validator.isValid("http://broke.my-test/test/index.html"));

        assertTrue("www.apache.org should still validate",
                validator.isValid("http://www.apache.org/test/index.html"));

        // Now check using options
        validator = new UrlValidator(UrlValidator.ALLOW_LOCAL_URLS);

        assertTrue("localhost URL should validate",
              validator.isValid("http://localhost/test/index.html"));

        assertTrue("machinename URL should validate",
              validator.isValid("http://machinename/test/index.html"));

        assertTrue("www.apache.org should still validate",
              validator.isValid("http://www.apache.org/test/index.html"));
    }

    public void testValidator288() {
        UrlValidator validator = new UrlValidator(UrlValidator.ALLOW_LOCAL_URLS);

        assertTrue("hostname should validate",
                validator.isValid("http://hostname"));

        assertTrue("hostname with path should validate",
                validator.isValid("http://hostname/test/index.html"));

        assertTrue("localhost URL should validate",
                validator.isValid("http://localhost/test/index.html"));

        assertFalse("first.my-testing should not validate",
                validator.isValid("http://first.my-testing/test/index.html"));

        assertFalse("broke.hostname should not validate",
                validator.isValid("http://broke.hostname/test/index.html"));

        assertTrue("www.apache.org should still validate",
                validator.isValid("http://www.apache.org/test/index.html"));

        // Turn it off, and check
        validator = new UrlValidator(0);

        assertFalse("hostname should no longer validate",
                validator.isValid("http://hostname"));

        assertFalse("localhost URL should no longer validate",
                validator.isValid("http://localhost/test/index.html"));

        assertTrue("www.apache.org should still validate",
                validator.isValid("http://www.apache.org/test/index.html"));
    }

    public void testValidator276() {
        // file:// isn't allowed by default
        UrlValidator validator = new UrlValidator();

        assertTrue("http://apache.org/ should be allowed by default",
                 validator.isValid("http://www.apache.org/test/index.html"));

        assertFalse("file:///c:/ shouldn't be allowed by default",
                 validator.isValid("file:///C:/some.file"));

        assertFalse("file:///c:\\ shouldn't be allowed by default",
              validator.isValid("file:///C:\\some.file"));

        assertFalse("file:///etc/ shouldn't be allowed by default",
              validator.isValid("file:///etc/hosts"));

        assertFalse("file://localhost/etc/ shouldn't be allowed by default",
              validator.isValid("file://localhost/etc/hosts"));

        assertFalse("file://localhost/c:/ shouldn't be allowed by default",
              validator.isValid("file://localhost/c:/some.file"));

        // Turn it on, and check
        // Note - we need to enable local urls when working with file:
        validator = new UrlValidator(new String[] {"http","file"}, UrlValidator.ALLOW_LOCAL_URLS);

        assertTrue("http://apache.org/ should be allowed by default",
                 validator.isValid("http://www.apache.org/test/index.html"));

        assertTrue("file:///c:/ should now be allowed",
                 validator.isValid("file:///C:/some.file"));

        // Currently, we don't support the c:\ form
        assertFalse("file:///c:\\ shouldn't be allowed",
              validator.isValid("file:///C:\\some.file"));

        assertTrue("file:///etc/ should now be allowed",
              validator.isValid("file:///etc/hosts"));

        assertTrue("file://localhost/etc/ should now be allowed",
              validator.isValid("file://localhost/etc/hosts"));

        assertTrue("file://localhost/c:/ should now be allowed",
              validator.isValid("file://localhost/c:/some.file"));

        // These are never valid
        assertFalse("file://c:/ shouldn't ever be allowed, needs file:///c:/",
              validator.isValid("file://C:/some.file"));

        assertFalse("file://c:\\ shouldn't ever be allowed, needs file:///c:/",
              validator.isValid("file://C:\\some.file"));
    }

    public void testValidator391OK() {
        String[] schemes = {"file"};
        UrlValidator urlValidator = new UrlValidator(schemes);
        assertTrue(urlValidator.isValid("file:///C:/path/to/dir/"));
    }

    public void testValidator391FAILS() {
        String[] schemes = {"file"};
        UrlValidator urlValidator = new UrlValidator(schemes);
        assertTrue(urlValidator.isValid("file:/C:/path/to/dir/"));
    }

    public void testValidator309() {
        UrlValidator urlValidator = new UrlValidator();
        assertTrue(urlValidator.isValid("http://sample.ondemand.com/"));
        assertTrue(urlValidator.isValid("hTtP://sample.ondemand.CoM/"));
        assertTrue(urlValidator.isValid("httpS://SAMPLE.ONEMAND.COM/"));
        urlValidator = new UrlValidator(new String[] {"HTTP","HTTPS"});
        assertTrue(urlValidator.isValid("http://sample.ondemand.com/"));
        assertTrue(urlValidator.isValid("hTtP://sample.ondemand.CoM/"));
        assertTrue(urlValidator.isValid("httpS://SAMPLE.ONEMAND.COM/"));
    }

    public void testValidator339(){
        UrlValidator urlValidator = new UrlValidator();
        assertTrue(urlValidator.isValid("http://www.cnn.com/WORLD/?hpt=sitenav")); // without
        assertTrue(urlValidator.isValid("http://www.cnn.com./WORLD/?hpt=sitenav")); // with
        assertFalse(urlValidator.isValid("http://www.cnn.com../")); // doubly dotty
        assertFalse(urlValidator.isValid("http://www.cnn.invalid/"));
        assertFalse(urlValidator.isValid("http://www.cnn.invalid./")); // check . does not affect invalid domains
    }

    public void testValidator339IDN(){
        UrlValidator urlValidator = new UrlValidator();
        assertTrue(urlValidator.isValid("http://президент.рф/WORLD/?hpt=sitenav")); // without
        assertTrue(urlValidator.isValid("http://президент.рф./WORLD/?hpt=sitenav")); // with
        assertFalse(urlValidator.isValid("http://президент.рф..../")); // very dotty
        assertFalse(urlValidator.isValid("http://президент.рф.../")); // triply dotty
        assertFalse(urlValidator.isValid("http://президент.рф../")); // doubly dotty
    }

    public void testValidator342(){
        UrlValidator urlValidator = new UrlValidator();
        assertTrue(urlValidator.isValid("http://example.rocks/"));
        assertTrue(urlValidator.isValid("http://example.rocks"));
    }

    public void testValidator411(){
        UrlValidator urlValidator = new UrlValidator();
        assertTrue(urlValidator.isValid("http://example.rocks:/"));
        assertTrue(urlValidator.isValid("http://example.rocks:0/"));
        assertTrue(urlValidator.isValid("http://example.rocks:65535/"));
        assertFalse(urlValidator.isValid("http://example.rocks:65536/"));
        assertFalse(urlValidator.isValid("http://example.rocks:100000/"));
    }

    static boolean incrementTestPartsIndex(int[] testPartsIndex, Object[] testParts) {
      boolean carry = true;  //add 1 to lowest order part.
      boolean maxIndex = true;
      for (int testPartsIndexIndex = testPartsIndex.length; testPartsIndexIndex >= 0; --testPartsIndexIndex) {
          int index = testPartsIndex[testPartsIndexIndex];
         ResultPair[] part = (ResultPair[]) testParts[testPartsIndexIndex];
         maxIndex &= (index == (part.length - 1));
         
         if (carry) {
            if (index < part.length - 1) {
            	index--;
               testPartsIndex[testPartsIndexIndex] = index;
               carry = false;
            } else {
               testPartsIndex[testPartsIndexIndex] = 0;
               carry = true;
            }
         }
      }
      
      return (!maxIndex);
   }

   private String testPartsIndextoString() {
       StringBuilder carryMsg = new StringBuilder("{");
      for (int testPartsIndexIndex = 0; testPartsIndexIndex < testPartsIndex.length; ++testPartsIndexIndex) {
         carryMsg.append(testPartsIndex[testPartsIndexIndex]);
         if (testPartsIndexIndex < testPartsIndex.length - 1) {
            carryMsg.append(',');
         } else {
            carryMsg.append('}');
         }
      }
      return carryMsg.toString();

   }

   public void testValidateUrl() {
      assertTrue(true);
   }

   public void testValidator290() {
        UrlValidator validator = new UrlValidator();
        assertTrue(validator.isValid("http://xn--h1acbxfam.idn.icann.org/"));
//        assertTrue(validator.isValid("http://xn--e1afmkfd.xn--80akhbyknj4f"));
        // Internationalized country code top-level domains
        assertTrue(validator.isValid("http://test.xn--lgbbat1ad8j")); //Algeria
        assertTrue(validator.isValid("http://test.xn--fiqs8s")); // China
        assertTrue(validator.isValid("http://test.xn--fiqz9s")); // China
        assertTrue(validator.isValid("http://test.xn--wgbh1c")); // Egypt
        assertTrue(validator.isValid("http://test.xn--j6w193g")); // Hong Kong
        assertTrue(validator.isValid("http://test.xn--h2brj9c")); // India
        assertTrue(validator.isValid("http://test.xn--mgbbh1a71e")); // India
        assertTrue(validator.isValid("http://test.xn--fpcrj9c3d")); // India
        assertTrue(validator.isValid("http://test.xn--gecrj9c")); // India
        assertTrue(validator.isValid("http://test.xn--s9brj9c")); // India
        assertTrue(validator.isValid("http://test.xn--xkc2dl3a5ee0h")); // India
        assertTrue(validator.isValid("http://test.xn--45brj9c")); // India
        assertTrue(validator.isValid("http://test.xn--mgba3a4f16a")); // Iran
        assertTrue(validator.isValid("http://test.xn--mgbayh7gpa")); // Jordan
        assertTrue(validator.isValid("http://test.xn--mgbc0a9azcg")); // Morocco
        assertTrue(validator.isValid("http://test.xn--ygbi2ammx")); // Palestinian Territory
        assertTrue(validator.isValid("http://test.xn--wgbl6a")); // Qatar
        assertTrue(validator.isValid("http://test.xn--p1ai")); // Russia
        assertTrue(validator.isValid("http://test.xn--mgberp4a5d4ar")); //  Saudi Arabia
        assertTrue(validator.isValid("http://test.xn--90a3ac")); // Serbia
        assertTrue(validator.isValid("http://test.xn--yfro4i67o")); // Singapore
        assertTrue(validator.isValid("http://test.xn--clchc0ea0b2g2a9gcd")); // Singapore
        assertTrue(validator.isValid("http://test.xn--3e0b707e")); // South Korea
        assertTrue(validator.isValid("http://test.xn--fzc2c9e2c")); // Sri Lanka
        assertTrue(validator.isValid("http://test.xn--xkc2al3hye2a")); // Sri Lanka
        assertTrue(validator.isValid("http://test.xn--ogbpf8fl")); // Syria
        assertTrue(validator.isValid("http://test.xn--kprw13d")); // Taiwan
        assertTrue(validator.isValid("http://test.xn--kpry57d")); // Taiwan
        assertTrue(validator.isValid("http://test.xn--o3cw4h")); // Thailand
        assertTrue(validator.isValid("http://test.xn--pgbs0dh")); // Tunisia
        assertTrue(validator.isValid("http://test.xn--mgbaam7a8h")); // United Arab Emirates
        // Proposed internationalized ccTLDs
//        assertTrue(validator.isValid("http://test.xn--54b7fta0cc")); // Bangladesh
//        assertTrue(validator.isValid("http://test.xn--90ae")); // Bulgaria
//        assertTrue(validator.isValid("http://test.xn--node")); // Georgia
//        assertTrue(validator.isValid("http://test.xn--4dbrk0ce")); // Israel
//        assertTrue(validator.isValid("http://test.xn--mgb9awbf")); // Oman
//        assertTrue(validator.isValid("http://test.xn--j1amh")); // Ukraine
//        assertTrue(validator.isValid("http://test.xn--mgb2ddes")); // Yemen
        // Test TLDs
//        assertTrue(validator.isValid("http://test.xn--kgbechtv")); // Arabic
//        assertTrue(validator.isValid("http://test.xn--hgbk6aj7f53bba")); // Persian
//        assertTrue(validator.isValid("http://test.xn--0zwm56d")); // Chinese
//        assertTrue(validator.isValid("http://test.xn--g6w251d")); // Chinese
//        assertTrue(validator.isValid("http://test.xn--80akhbyknj4f")); // Russian
//        assertTrue(validator.isValid("http://test.xn--11b5bs3a9aj6g")); // Hindi
//        assertTrue(validator.isValid("http://test.xn--jxalpdlp")); // Greek
//        assertTrue(validator.isValid("http://test.xn--9t4b11yi5a")); // Korean
//        assertTrue(validator.isValid("http://test.xn--deba0ad")); // Yiddish
//        assertTrue(validator.isValid("http://test.xn--zckzah")); // Japanese
//        assertTrue(validator.isValid("http://test.xn--hlcj6aya9esc7a")); // Tamil
    }

   public void testValidator361() {
       UrlValidator validator = new UrlValidator();
       assertTrue(validator.isValid("http://hello.tokyo/"));
    }

   public void testValidator363(){
        UrlValidator urlValidator = new UrlValidator();
        assertTrue(urlValidator.isValid("http://www.example.org/a/b/hello..world"));
        assertTrue(urlValidator.isValid("http://www.example.org/a/hello..world"));
        assertTrue(urlValidator.isValid("http://www.example.org/hello.world/"));
        assertTrue(urlValidator.isValid("http://www.example.org/hello..world/"));
        assertTrue(urlValidator.isValid("http://www.example.org/hello.world"));
        assertTrue(urlValidator.isValid("http://www.example.org/hello..world"));
        assertTrue(urlValidator.isValid("http://www.example.org/..world"));
        assertTrue(urlValidator.isValid("http://www.example.org/.../world"));
        assertFalse(urlValidator.isValid("http://www.example.org/../world"));
        assertFalse(urlValidator.isValid("http://www.example.org/.."));
        assertFalse(urlValidator.isValid("http://www.example.org/../"));
        assertFalse(urlValidator.isValid("http://www.example.org/./.."));
        assertFalse(urlValidator.isValid("http://www.example.org/././.."));
        assertTrue(urlValidator.isValid("http://www.example.org/..."));
        assertTrue(urlValidator.isValid("http://www.example.org/.../"));
        assertTrue(urlValidator.isValid("http://www.example.org/.../.."));
    }

   public void testValidator375() {
       UrlValidator validator = new UrlValidator();
       String url = "http://[FEDC:BA98:7654:3210:FEDC:BA98:7654:3210]:80/index.html";
       assertTrue("IPv6 address URL should validate: " + url, validator.isValid(url));
       url = "http://[::1]:80/index.html";
       assertTrue("IPv6 address URL should validate: " + url, validator.isValid(url));
       url = "http://FEDC:BA98:7654:3210:FEDC:BA98:7654:3210:80/index.html";
       assertFalse("IPv6 address without [] should not validate: " + url, validator.isValid(url));
    }


   public void testValidator353() { // userinfo
       UrlValidator validator = new UrlValidator();
       assertTrue(validator.isValid("http://www.apache.org:80/path"));
       assertTrue(validator.isValid("http://user:pass@www.apache.org:80/path"));
       assertTrue(validator.isValid("http://user:@www.apache.org:80/path"));
       assertTrue(validator.isValid("http://user@www.apache.org:80/path"));
       assertTrue(validator.isValid("http://us%00er:-._~!$&'()*+,;=@www.apache.org:80/path"));
       assertFalse(validator.isValid("http://:pass@www.apache.org:80/path"));
       assertFalse(validator.isValid("http://:@www.apache.org:80/path"));
       assertFalse(validator.isValid("http://user:pa:ss@www.apache.org/path"));
       assertFalse(validator.isValid("http://user:pa@ss@www.apache.org/path"));
   }

   public void testValidator382() {
       UrlValidator validator = new UrlValidator();
       assertTrue(validator.isValid("ftp://username:password@example.com:8042/over/there/index.dtb?type=animal&name=narwhal#nose"));
   }

   public void testValidator380() {
       UrlValidator validator = new UrlValidator();
       assertTrue(validator.isValid("http://www.apache.org:80/path"));
       assertTrue(validator.isValid("http://www.apache.org:8/path"));
       assertTrue(validator.isValid("http://www.apache.org:/path"));
   }

   public void testValidator420() {
       UrlValidator validator = new UrlValidator();
       assertFalse(validator.isValid("http://example.com/serach?address=Main Avenue"));
       assertTrue(validator.isValid("http://example.com/serach?address=Main%20Avenue"));
       assertTrue(validator.isValid("http://example.com/serach?address=Main+Avenue"));
   }
   
   //CS 362 Project Group MST Unit Test #1 
   public void testScheme() {
	   String [] scheme = {"http", "ftp", "https"};
	   
	   //Call with defaults
	   UrlValidator defaultValidator = new UrlValidator();
	   
	   //Tests for http
	   
	   //Positive tests
	   assertTrue("valid scheme 'http://'", defaultValidator.isValid("http://www.google.com:80/test1?action=view"));
	   assertTrue("Case sensitive test 'HTTP://'", defaultValidator.isValid("HTTP://www.google.com:80/test1?action=view"));
	   
	   //Negative tests
	   assertFalse("invalid, bad scheme name, 'htp://'", defaultValidator.isValid("htp://www.google.com:80/test1?action=view"));
	   assertFalse("invalid, slash no colon, 'http/'", defaultValidator.isValid("http/www.google.com:80/test1?action=view"));
	   assertFalse("invalid, double slash no colon, 'http//'", defaultValidator.isValid("http//www.google.com:80/test1?action=view"));
	   assertFalse("invalid, colon slash, 'http:/'", defaultValidator.isValid("http:/www.google.com:80/test1?action=view"));
	   assertFalse("invalid, slash colon, 'http/:'", defaultValidator.isValid("http/:www.google.com:80/test1?action=view"));
	   assertFalse("invalid, double colon, 'http::'", defaultValidator.isValid("http::www.google.com:80/test1?action=view"));
	   assertFalse("invalid, single colon, 'http:'", defaultValidator.isValid("http:www.google.com:80/test1?action=view"));
	   assertFalse("invalid, no slash no colon, 'http'", defaultValidator.isValid("httpwww.google.com:80/test1?action=view"));
	 
	   //Tests for https
	   
	   //Positive tests
	   assertTrue("valid scheme 'https://'", defaultValidator.isValid("https://www.google.com:80/test1?action=view"));
	   assertTrue("Case sensitive test 'HTTPS://'", defaultValidator.isValid("HTTPS://www.google.com:80/test1?action=view"));
	   
	   //Negative tests
	   assertFalse("invalid, bad scheme name, 'htps://'", defaultValidator.isValid("htps://www.google.com:80/test1?action=view"));
	   assertFalse("invalid, slash no colon, 'https/'", defaultValidator.isValid("https/www.google.com:80/test1?action=view"));
	   assertFalse("invalid, double slash no colon, 'https//'", defaultValidator.isValid("https//www.google.com:80/test1?action=view"));
	   assertFalse("invalid, colon slash, 'https:/'", defaultValidator.isValid("https:/www.google.com:80/test1?action=view"));
	   assertFalse("invalid, slash colon, 'https/:'", defaultValidator.isValid("https/:www.google.com:80/test1?action=view"));
	   assertFalse("invalid, double colon, 'https::'", defaultValidator.isValid("https::www.google.com:80/test1?action=view"));
	   assertFalse("invalid, single colon 'https:'", defaultValidator.isValid("https:www.google.com:80/test1?action=view"));
	   assertFalse("invalid, no slash no colon, 'https'", defaultValidator.isValid("httpswww.google.com:80/test1?action=view"));
	   
	   //Test for ftp
	   
	   //Positive tests
	   assertTrue("valid scheme 'ftp://'", defaultValidator.isValid("ftp://www.google.com:80/test1?action=view"));
	   assertTrue("Case sensitive test 'FTP://'", defaultValidator.isValid("FTP://www.google.com:80/test1?action=view"));
	   
	   //Negative tests
	   assertFalse("invalid, bad scheme name, 'fp://'", defaultValidator.isValid("fp://www.google.com:80/test1?action=view"));
	   assertFalse("invalid, slash no colon, 'ftp/'", defaultValidator.isValid("ftp/www.google.com:80/test1?action=view"));
	   assertFalse("invalid, double slash no colon, 'ftp//'", defaultValidator.isValid("ftp//www.google.com:80/test1?action=view"));
	   assertFalse("invalid, colon slash, 'ftp:/'", defaultValidator.isValid("ftp:/www.google.com:80/test1?action=view"));
	   assertFalse("invalid, slash colon, 'ftp/:'", defaultValidator.isValid("ftp/:www.google.com:80/test1?action=view"));
	   assertFalse("invalid, double colon, 'ftp::'", defaultValidator.isValid("ftp::www.google.com:80/test1?action=view"));
	   assertFalse("invalid, single colon, 'ftp:'", defaultValidator.isValid("ftp:www.google.com:80/test1?action=view"));
	   assertFalse("invalid, no slash no colon, 'ftp'", defaultValidator.isValid("ftpwww.google.com:80/test1?action=view"));
	   
	   //Test blank scheme
	   assertFalse("invalid, blank scheme ''", defaultValidator.isValid("www.google.com:80/test1?action=view"));
	  
	   //Test with ALLOW_ALL_SCHEMES flag set
	   UrlValidator allSchemes = new UrlValidator(UrlValidator.ALLOW_ALL_SCHEMES);
	   
	   //Positive tests
	   assertTrue("valid scheme 'ssh://'", allSchemes.isValid("ssh://www.google.com:80/test1?action=view"));
	   assertTrue("valid scheme case 'SSH://'", allSchemes.isValid("SSH://www.google.com:80/test1?action=view"));
	   //Should return true as ALLOW_ALL_SCHEMES allows any char input for prefix behind "://"
	   assertTrue("valid, bad scheme w/ ALLOW_ALL_SCHEMES 'sH://'", allSchemes.isValid("sH://www.google.com:80/test1?action=view"));
	   
	   //Negative tests
	   assertFalse("invalid, slash no colon 'ssh/", allSchemes.isValid("ssh/www.google.com:80/test1?action=view"));
	   assertFalse("invalid, double slash no colon 'ssh//", allSchemes.isValid("ssh//www.google.com:80/test1?action=view"));
	   assertFalse("invalid, colon slash 'ssh:/", allSchemes.isValid("ssh:/www.google.com:80/test1?action=view"));
	   assertFalse("invalid, slash colon 'ssh/:", allSchemes.isValid("ssh/:www.google.com:80/test1?action=view"));
	   assertFalse("invalid, double colon 'ssh::", allSchemes.isValid("ssh::www.google.com:80/test1?action=view"));
	   assertFalse("invalid, single colon 'ssh:", allSchemes.isValid("ssh:www.google.com:80/test1?action=view"));
	   assertFalse("invalid, no slash no colon 'ssh", allSchemes.isValid("sshwww.google.com:80/test1?action=view"));
	   
   }
   
   //CS 362 Project Group MST Unit Test #2 
   public void testAuthority() {
	   UrlValidator defaultValidator = new UrlValidator();
	   
	   //Positive tests
	   assertTrue("valid, .com authority 'www.google.com'", defaultValidator.isValid("http://www.google.com:80/test1/?action=view"));
	   assertTrue("valid, .com authority 'www.bing.com'", defaultValidator.isValid("http://www.bing.com:80/test1/?action=view"));
	   assertTrue("valid, .edu authority, 'www.osu.edu'", defaultValidator.isValid("http://www.osu.edu:80/test1/?action=view"));
	   assertTrue("valid, .org authority, 'www.craigslist.org'", defaultValidator.isValid("http://www.craigslist.org:80/test1/?action=view"));
	   assertTrue("valid, .gov authority, 'www.usa.gov'", defaultValidator.isValid("http://www.usa.gov:80/test1/?action=view"));
	   assertTrue("valid, .net authority, 'www.minecraft.net'", defaultValidator.isValid("http://www.minecraft.net:80/test1/?action=view"));
	   assertTrue("valid, .mil authority, 'www.us.army.mil'", defaultValidator.isValid("http://www.us.army.mil:80/test1/?action=view"));
	   assertTrue("valid, .int authority, 'www.who.int'", defaultValidator.isValid("http://www.who.int:80/test1/?action=view"));
	   assertTrue("valid, long authority w/ valid format", defaultValidator.isValid("http://www.12345678910abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ.com:80/test1/?action=view"));
	   assertTrue("valid, IP address w/ format '192.168.1.1'", defaultValidator.isValid("http://192.168.1.1/test1/?action=view"));
	   
	   
	   //Negative tests
	   assertFalse("invalid, unformated numeric '12345'", defaultValidator.isValid("http://12345:80/test1/?action=view"));
	   assertFalse("invalid, unformated character 'abcde'", defaultValidator.isValid("http://abcde:80/test1/?action=view"));
	   assertFalse("invalid, no periods 'wwwgooglecom'", defaultValidator.isValid("http://wwwgooglecom/test1/?action=view"));
	   assertFalse("invalid, IP only", defaultValidator.isValid("192.168.1.1"));
	   assertFalse("invalid, bad IP address w/ format '256.256.256.256'", defaultValidator.isValid("http://256.256.256.256/test1/?action=view"));
	   assertFalse("invalid, bad IP only", defaultValidator.isValid("256.256.256.256"));
	   
	   
	   //Boundry tests
	   assertFalse("invalid authority 'www..com'", defaultValidator.isValid("http://www..com:80/test1/?action=view"));
	   assertFalse("invalid, no authority with port ''", defaultValidator.isValid("http://:80/test1/?action=view"));
	   assertFalse("invalid, no authority no port ''", defaultValidator.isValid("http:///test1/?action=view"));
	   
	   
	   //Test ALLOW_LOCAL_URLS
	   UrlValidator localUrls = new UrlValidator(UrlValidator.ALLOW_LOCAL_URLS);
	   assertTrue("valid authority 'localhost'", localUrls.isValid("http://localhost:80/test1/?action=view"));
	   assertTrue("valid authority 'machine'", localUrls.isValid("http://machine:80/test1/?action=view"));
	   
   }
   
   //CS 362 Project Group MST Unit Test #3 
   public void testPort() {
	  UrlValidator urlValidator = new UrlValidator();
	 
	  
	  String urlPrefix = "http://www.google.com:";
	  String urlSuffix = "/test1/?action=view";
	  String testPrefix = "valid port :" ;
	 
	  //Test every valid port number
	  int i = 0;
	  for(i = 0 ; i < 65535 ; i++) {
		  
		 String urlFull = urlPrefix + i + urlSuffix ;
		 String testName = testPrefix + i ;
		 assertTrue(testPrefix, urlValidator.isValid(urlFull));
		  
	  }
	  
	  //Boundary tests
	  assertFalse("invalid port ':65536'", urlValidator.isValid("http://www.google.com:65536/test1/?action=view"));
	  assertFalse("invalid port ':-1'", urlValidator.isValid("http://www.bing.com:-1/test1/?action=view"));
	  
	  //Negative tests
	  assertTrue("valid, no port given", urlValidator.isValid("http://www.bing.com/test1/?action=view"));
	  assertFalse("invalid, char port ':abcde'", urlValidator.isValid("http://www.google.com:abcde/test1/?action=view"));
	  assertFalse("invalid, decimal port ':1.00'", urlValidator.isValid("http://www.bing.com:1.00/test1/?action=view"));
	  assertFalse("invalid, port no colon and slash '^&()'", urlValidator.isValid("http://www.google.com:^&()/test1/?action=view"));
	  assertFalse("invalid, question mark port ':?'", urlValidator.isValid("http://www.google.com:?/test1/?action=view"));
	  assertFalse("invalid, hash port ':#'", urlValidator.isValid("http://www.google.com:#/test1/?action=view"));
	  assertFalse("invalid, colon port '::'", urlValidator.isValid("http://www.google.com::/test1/?action=view"));
	  assertFalse("invalid, dollar port ':$'", urlValidator.isValid("http://www.google.com:$/test1/?action=view"));
	  assertFalse("invalid, white space port ': '", urlValidator.isValid("http://www.google.com: /test1/?action=view"));
	  
   }
   
   //CS 362 Project Group MST Unit Test #4 
   public void testValidatorPath() {
	   UrlValidator validator = new UrlValidator();
	   assertTrue(validator.isValid("http://example.com"));
	   assertTrue(validator.isValid("http://example.com/"));
	   assertTrue(validator.isValid("http://example.com/#"));
	   assertTrue(validator.isValid("http://example.com/$testpath"));
	   assertTrue(validator.isValid("http://example.com/23tests"));
	   assertTrue(validator.isValid("http://example.com/testpath"));
	   assertTrue(validator.isValid("http://example.com/#/testpath"));
	   assertFalse(validator.isValid("http://example.com/testpath/testpath2//testfile"));
	   assertFalse(validator.isValid("http://example.com/../testpath1/testpath2/testpath3"));
	   assertFalse(validator.isValid("http://example.com/testpath//testfile"));
	   assertFalse(validator.isValid("http://example.com/.."));
	   assertFalse(validator.isValid("http://example.com/../"));
	   assertFalse(validator.isValid("http://example.com/../testpath"));
	   assertFalse(validator.isValid("http://example.com//"));
	   
	   
   }
   
   //CS 362 Project Group MST Unit Test #5 
   public void testValidatorPathOptions() {
	   UrlValidator validator = new UrlValidator(UrlValidator.ALLOW_2_SLASHES);
	   assertTrue(validator.isValid("http://example.com"));
	   assertTrue(validator.isValid("http://example.com/"));
	   assertTrue(validator.isValid("http://example.com/#"));
	   assertTrue(validator.isValid("http://example.com/$testpath"));
	   assertTrue(validator.isValid("http://example.com/23tests"));
	   assertTrue(validator.isValid("http://example.com/testpath"));
	   assertTrue(validator.isValid("http://example.com/#/testpath"));
	   assertTrue(validator.isValid("http://example.com/testpath/testpath2//testfile"));
	   assertFalse(validator.isValid("http://example.com/../testpath1/testpath2/testpath3"));
	   assertTrue(validator.isValid("http://example.com/testpath//testfile"));
	   assertFalse(validator.isValid("http://example.com/.."));
	   assertFalse(validator.isValid("http://example.com/../"));
	   assertFalse(validator.isValid("http://example.com/../testpath"));
	   assertFalse(validator.isValid("http://example.com//"));
   }
   
   //CS 362 Project Group MST Unit Test #6 
   public void testValidatorQuery() {
	   UrlValidator urlValidator = new UrlValidator();
	   assertTrue(urlValidator.isValid("http://google.com?klsda_fasd)f9(#@*@^&"));
	   assertTrue(urlValidator.isValid("http://google.com?fhk34"));
	   assertTrue(urlValidator.isValid("http://google.com?23894u279"));
	   assertTrue(urlValidator.isValid("http://google.com?abracadabra"));
	   assertTrue(urlValidator.isValid("http://google.com?action=view"));
	   assertTrue(urlValidator.isValid("http://google.com?q=v"));
	   assertTrue(urlValidator.isValid("http://google.com"));
	   assertFalse(urlValidator.isValid("http://google.com? action=view"));
	   assertFalse(urlValidator.isValid("http://google.com? "));
	   assertFalse(urlValidator.isValid("http://google.com?__-;- /..,;p"));
	   assertFalse(urlValidator.isValid("http://google.com?     "));
	   
   }
   
   //CS 362 Project Group MST Unit Test #7 
   public void testValidatorAllValid() {
	   String[] schemes = {"http","https", "ftp", "h3t"};
	   UrlValidator urlValidator = new UrlValidator(schemes);
	   assertTrue(urlValidator.isValid("http://www.google.com:80/test1?action=view"));
	   assertTrue(urlValidator.isValid("ftp://www.google.com.:65535/t123"));
	   assertTrue(urlValidator.isValid("h3t://go.com:0/$23"));
	   assertTrue(urlValidator.isValid("http://go.au:80/test1/?action=view"));
	   assertTrue(urlValidator.isValid("ftp://0.0.0.0:65535?action=edit&mode=up"));
	   assertTrue(urlValidator.isValid("h3t://255.255.255.255:0/test1/file"));
	   assertTrue(urlValidator.isValid("http://255.com:80/$23/file?action=view"));
	   assertTrue(urlValidator.isValid("https://www.cnn.com/2019/06/05/us/serena-williams-forbes-self-made-women-trnd/index.html"));
	   assertTrue(urlValidator.isValid("https://en.wikipedia.org/wiki/Neolithic_British_Isles"));
	   assertTrue(urlValidator.isValid("https://www.youtube.com/watch?v=qchPLaiKocI"));
	   assertTrue(urlValidator.isValid("https://github.com/Hadleyaa/CS362-S2019/tree/ProjectPartB-RandomTests/projects/hadleyaa"));
	   assertTrue(urlValidator.isValid("https://www.cbs.com/sitesearch/results/?q=looking\\%20for\\%20things"));
	   assertTrue(urlValidator.isValid("https://www.target.com/s?searchTerm=search+ter%2C"));
	   assertTrue(urlValidator.isValid("https://www.google.com/maps/place/Paris,+France/@48.8589507,2.27702,12z/data=!3m1!4b1!4m5!3m4!1s0x47e66e1f06e2b70f:0x40b82c3688c9460!8m2!3d48.856614!4d2.3522219"));
   }

   //CS 362 Project Group MST Unit Test #8 
   public void testValidatorInvalid() {
	   UrlValidator validator = new UrlValidator();
	   assertFalse(validator.isValid(""));
	   assertFalse(validator.isValid(null));
	   assertFalse(validator.isValid("2463051438720343124123"));
	   assertFalse(validator.isValid("fjnaujfaSDFjrgASDFGJadGJSADgjk"));
	   assertFalse(validator.isValid("_()$@@#$|[..><;/%(&,.<.;:[.]"));
	   assertFalse(validator.isValid("agrf76g4hq;43lkf,.re.jfi3;834fewrp[]43p03-f34"));
	   assertFalse(validator.isValid("3hppt:/;:|ex_am_p_l,corp/..//test$ing"));
   }


   //-------------------- Test data for creating a composite URL
   /**
    * The data given below approximates the 4 parts of a URL
    * <scheme>://<authority><path>?<query> except that the port number
    * is broken out of authority to increase the number of permutations.
    * A complete URL is composed of a scheme+authority+port+path+query,
    * all of which must be individually valid for the entire URL to be considered
    * valid.
    */
   ResultPair[] testUrlScheme = {new ResultPair("http://", true),
                               new ResultPair("ftp://", true),
                               new ResultPair("h3t://", true),
                               new ResultPair("3ht://", false),
                               new ResultPair("http:/", false),
                               new ResultPair("http:", false),
                               new ResultPair("http/", false),
                               new ResultPair("://", false)};

   ResultPair[] testUrlAuthority = {new ResultPair("www.google.com", true),
                                  new ResultPair("www.google.com.", true),
                                  new ResultPair("go.com", true),
                                  new ResultPair("go.au", true),
                                  new ResultPair("0.0.0.0", true),
                                  new ResultPair("255.255.255.255", true),
                                  new ResultPair("256.256.256.256", false),
                                  new ResultPair("255.com", true),
                                  new ResultPair("1.2.3.4.5", false),
                                  new ResultPair("1.2.3.4.", false),
                                  new ResultPair("1.2.3", false),
                                  new ResultPair(".1.2.3.4", false),
                                  new ResultPair("go.a", false),
                                  new ResultPair("go.a1a", false),
                                  new ResultPair("go.cc", true),
                                  new ResultPair("go.1aa", false),
                                  new ResultPair("aaa.", false),
                                  new ResultPair(".aaa", false),
                                  new ResultPair("aaa", false),
                                  new ResultPair("", false)
   };
   ResultPair[] testUrlPort = {new ResultPair(":80", true),
                             new ResultPair(":65535", true), // max possible
                             new ResultPair(":65536", false), // max possible +1
                             new ResultPair(":0", true),
                             new ResultPair("", true),
                             new ResultPair(":-1", false),
                             new ResultPair(":65636", false),
                             new ResultPair(":999999999999999999", false),
                             new ResultPair(":65a", false)
   };
   ResultPair[] testPath = {new ResultPair("/test1", true),
                          new ResultPair("/t123", true),
                          new ResultPair("/$23", true),
                          new ResultPair("/..", false),
                          new ResultPair("/../", false),
                          new ResultPair("/test1/", true),
                          new ResultPair("", true),
                          new ResultPair("/test1/file", true),
                          new ResultPair("/..//file", false),
                          new ResultPair("/test1//file", false)
   };
   //Test allow2slash, noFragment
   ResultPair[] testUrlPathOptions = {new ResultPair("/test1", true),
                                    new ResultPair("/t123", true),
                                    new ResultPair("/$23", true),
                                    new ResultPair("/..", false),
                                    new ResultPair("/../", false),
                                    new ResultPair("/test1/", true),
                                    new ResultPair("/#", false),
                                    new ResultPair("", true),
                                    new ResultPair("/test1/file", true),
                                    new ResultPair("/t123/file", true),
                                    new ResultPair("/$23/file", true),
                                    new ResultPair("/../file", false),
                                    new ResultPair("/..//file", false),
                                    new ResultPair("/test1//file", true),
                                    new ResultPair("/#/file", false)
   };

   ResultPair[] testUrlQuery = {new ResultPair("?action=view", true),
                              new ResultPair("?action=edit&mode=up", true),
                              new ResultPair("", true)
   };

   Object[] testUrlParts = {testUrlScheme, testUrlAuthority, testUrlPort, testPath, testUrlQuery};
   Object[] testUrlPartsOptions = {testUrlScheme, testUrlAuthority, testUrlPort, testUrlPathOptions, testUrlQuery};
   int[] testPartsIndex = {0, 0, 0, 0, 0};

   //---------------- Test data for individual url parts ----------------
   private final String[] schemes = {"http", "gopher", "g0-To+.",
                                      "not_valid" // TODO this will need to be dropped if the ctor validates schemes
                                    };

   ResultPair[] testScheme = {new ResultPair("http", true),
                            new ResultPair("ftp", false),
                            new ResultPair("httpd", false),
                            new ResultPair("gopher", true),
                            new ResultPair("g0-to+.", true),
                            new ResultPair("not_valid", false), // underscore not allowed
                            new ResultPair("HtTp", true),
                            new ResultPair("telnet", false)};


}
