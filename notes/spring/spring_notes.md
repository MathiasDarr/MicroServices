### Spring Notes ###

HttpServletRequestWrapper

HttpServletRequest is an interface for a HTTP specific servlet request. Typically you get instances of this interface in servlet filters or servlets.

Sometimes you want to adjust the original request at some point. With a HttpServletRequestWrapper you can wrap the original request and overwrite some methods so that it behaves slightly different. 