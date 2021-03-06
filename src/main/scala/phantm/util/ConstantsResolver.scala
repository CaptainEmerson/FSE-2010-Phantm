package phantm.util
import phantm.symbols._
import phantm.types.TypeHelpers
import phantm.types.TAny
import phantm.phases.PhasesContext
import phantm.Settings

import phantm.ast.Trees._
import phantm.ast.ASTTransform

case class ConstantsResolver(ast: Program, issueErrors: Boolean, ctx: PhasesContext) extends ASTTransform(ast) {

    override def trExpr(ex: Expression): Expression = ex match {
        case FunctionCall(StaticFunctionRef(NSName("define")), List(CallArg(PHPString(name), _), CallArg(expr, _))) =>
            ctx.globalSymbols.lookupConstant(name) match {
                case None =>
                    Evaluator.staticEval(expr, ctx, issueErrors) match {
                        case Some(v) =>
                            val cs = new ConstantSymbol(name, Some(v))
                            cs.typ = TypeHelpers.exprToType(v)

                            ctx.globalSymbols.registerConstant(cs)
                        case None =>
                            if (issueErrors && Settings.get.verbosity >= 2) {
                                Reporter.notice("Dynamic constant declaration", expr)
                            }

                            if(issueErrors) {
                                val cs = new ConstantSymbol(name, None)
                                ctx.globalSymbols.registerConstant(cs)
                            }
                    }
                case Some(_) =>
                    // already defined: ignore
            }
            ex



        case FunctionCall(StaticFunctionRef(NSName("define")), _) =>
            if (issueErrors && Settings.get.verbosity >= 2) {
                Reporter.notice("Dynamic constant declaration ignored", ex)
            }
            ex

        case _ => super.trExpr(ex)
    }

  override def trStmt(st: Statement): Statement = st match {
    case ConstantDecl(NSName(name), expr) =>
      ctx.globalSymbols.lookupConstant(name) match {
        case None =>
          Evaluator.staticEval(expr, ctx, issueErrors) match {
            case Some(v) =>
              val cs = new ConstantSymbol(name, Some(v))
              cs.typ = TypeHelpers.exprToType(v)

              ctx.globalSymbols.registerConstant(cs)
            case None =>
              if (issueErrors && Settings.get.verbosity >= 2) {
                Reporter.notice("Dynamic constant declaration", st)
              }

              if(issueErrors) {
                val cs = new ConstantSymbol(name, None)
                ctx.globalSymbols.registerConstant(cs)
              }
          }
        case Some(_) =>
          if (issueErrors && Settings.get.verbosity >= 2) {
            Reporter.notice("Constant '"+name+"' already declared", st)
          }
      }

      new Void().setPos(st).annotateFromC(st)
    case _ =>
      super.trStmt(st)
  }

}
