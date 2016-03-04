/**
 * Copyright (c) 2015, Impending
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * @license     Berkeley Software Distribution License (BSD-License 2) http://www.opensource.org/licenses/bsd-license.php
 * @author      Freddie Tilley <freddie.tilley@impending.nl>
 * @copyright   Impending
 * @link        http://www.impending.nl
 */
package com.mollie.api.objects;

import java.math.BigDecimal;

public class Method {
    /**
     * @see <a href="https://mollie.com/ideal">https://mollie.com/ideal</a>
     */
    public static final String IDEAL                = "ideal";

    /**
     * @see <a href="https://mollie.com/paysafecard">https://mollie.com/paysafecard</a>
     */
    public static final String PAYSAFECARD          = "paysafecard";

    /**
     * @see <a href="https://mollie.com/creditcard">https://mollie.com/creditcard</a>
     */
    public static final String CREDITCARD           = "creditcard";

    /**
     * @see <a href="https://mollie.com/mistercash">https://mollie.com/mistercash</a>
     */
    public static final String MISTERCASH           = "mistercash";

    /**
     * @see <a href="https://mollie.com/sofort">https://mollie.com/sofort</a>
     */
    public static final String SOFORT               = "sofort";

    /**
     * @see <a href="https://mollie.com/banktransfer">https://mollie.com/banktransfer</a>
     */
    public static final String BANKTRANSFER         = "banktransfer";

    /**
     * @see <a href="https://mollie.com/directdebit">https://mollie.com/directdebit</a>
     */
    public static final String DIRECTDEBIT          = "directdebit";

    /**
     * @see <a href="https://mollie.com/paypal">https://mollie.com/paypal</a>
     */
    public static final String PAYPAL               = "paypal";

    /**
     * @see <a href="https://mollie.com/bitcoin">https://mollie.com/bitcoin</a>
     */
    public static final String BITCOIN              = "bitcoin";

    /**
     * @see <a href="https://mollie.com/belfiusdirectnet">https://mollie.com/belfiusdirectnet</a>
     */
    public static final String BELFIUS              = "belfius";

    /**
     * @see <a href="https://mollie.com/giftcards">https://mollie.com/giftcards</a>
     */
    public static final String PODIUMCADEAUKAART    = "podiumcadeaukaart";

    /**
     * Id of the payment method.
     */
    private String id;

    /**
     * More legible description of the payment method.
     */
    private String description;

    /**
     * The amount.minimum() and amount.maximum() supported by this method and the used API key.
     */
    private Amount amount;

    /**
     * The image.normal() and image.bigger() to display the payment method logo.
     *
     * @var object
     */
    private ImageGroup image;

    public Method() {
    }

    public String id() { return id; }
    public void setId(String anId) { id = anId; }

    public String description() { return description; }
    public void setDescription(String aDescription) { description = aDescription; }

    public BigDecimal getMinimumAmount() {
        return (amount != null ? amount.minimum() : null);
    }

    public BigDecimal getMaximumAmount() {
        return (amount != null ? amount.maximum() : null);
    }

    public Amount amount() { return amount; }

    public ImageGroup image() { return image; }

    public static class Amount {
        private BigDecimal minimum;
        private BigDecimal maximum;

        public BigDecimal minimum() { return minimum; }
        public BigDecimal maximum() { return maximum; }
    };

    public static class ImageGroup {
        private String normal;
        private String bigger;

        public String normal() { return normal; }
        public String bigger() { return bigger; }
    }
}
