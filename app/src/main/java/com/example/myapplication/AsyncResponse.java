/*
 *
 *
 *     Copyright/* *
 *               * CedCommerce
 *               *
 *               * NOTICE OF LICENSE
 *               *
 *               * This source file is subject to the End User License Agreement(EULA)
 *               * that is bundled with this package in the file LICENSE.txt.
 *               * It is also available through the world-wide-web at this URL:
 *               * http://cedcommerce.com/license-agreement.txt
 *               *
 *               * @category  Ced
 *               * @package   MultiVendor
 *               * @author    CedCommerce Core Team <connect@cedcommerce.com >
 *               * @copyright Copyright CEDCOMMERCE (http://cedcommerce.com/)
 *               * @license   http://cedcommerce.com/license-agreement.txt
 *
 *
 *
 *
 */

package com.example.myapplication;

import org.json.JSONException;

/**
 * Created by cedcoss on 9/16/2015.
 */
public interface AsyncResponse {
    void processFinish(Object output) throws JSONException;
}
