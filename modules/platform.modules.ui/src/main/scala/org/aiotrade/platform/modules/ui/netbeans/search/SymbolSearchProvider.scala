/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.aiotrade.platform.modules.ui.netbeans.search

import java.net.MalformedURLException
import java.net.URL
import org.aiotrade.lib.securities.Market
import org.aiotrade.platform.spi.hotkey.SearchProvider
import org.aiotrade.platform.spi.hotkey.SearchRequest
import org.aiotrade.platform.spi.hotkey.SearchResponse
import org.openide.awt.HtmlBrowser.URLDisplayer


class SymbolSearchProvider extends SearchProvider {

  private val symbols = Market.symbolsOf(Market.SHSE)

  /**
   * Method is called by infrastructure when search operation was requested.
   * Implementors should evaluate given request and fill response object with
   * apropriate results
   *
   * @param request Search request object that contains information what to search for
   * @param response Search response object that stores search results.
   *    Note that it's important to react to return value of SearchResponse.addResult(...) method
   *    and stop computation if false value is returned.
   */
  def evaluate(request: SearchRequest, response: SearchResponse) {

    for (symbol <- symbols if symbol.toLowerCase.indexOf(request.text.toLowerCase) != -1) {
      if (!response.addResult(new SymbolFoundResult(symbol), symbol)) {
        return
      }
    }
  }

  private class SymbolFoundResult(symbol: String) extends Runnable {

    private val url = "http://finance.yahoo.com/q?s=" + symbol

    def run {
      try {
        URLDisplayer.getDefault.showURL(new URL(url))
      } catch {case ex: MalformedURLException =>}
    }
  }

}