package piuk.blockchain.android.ui.linkbank

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.blockchain.nabu.models.data.BankPartner
import com.blockchain.nabu.models.data.LinkBankTransfer
import com.blockchain.nabu.models.data.YodleeAttributes
import kotlinx.android.synthetic.main.toolbar_general.*
import piuk.blockchain.android.R
import piuk.blockchain.android.simplebuy.ErrorState
import piuk.blockchain.android.ui.base.BlockchainActivity
import piuk.blockchain.android.ui.linkbank.yodlee.LinkBankFragment
import piuk.blockchain.android.ui.linkbank.yodlee.YodleeLinkingFlowNavigator
import piuk.blockchain.android.ui.linkbank.yodlee.YodleeSplashFragment
import piuk.blockchain.android.ui.linkbank.yodlee.YodleeWebViewFragment

class LinkBankActivity : BlockchainActivity(), YodleeLinkingFlowNavigator {

    private val linkBankTransfer: LinkBankTransfer
        get() = intent.getSerializableExtra(LINK_BANK_TRANSFER_KEY) as LinkBankTransfer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_activity)
        setSupportActionBar(toolbar_general)
        if (savedInstanceState == null) {
            checkPartnerAndLaunchFlow(linkBankTransfer)
        }
    }

    private fun checkPartnerAndLaunchFlow(linkBankTransfer: LinkBankTransfer) {
        when (linkBankTransfer.partner) {
            BankPartner.YODLEE -> {
                val attributes = linkBankTransfer.attributes as YodleeAttributes
                launchYodleeSplash(attributes, linkBankTransfer.id)
            }
        }
    }

    override val alwaysDisableScreenshots: Boolean
        get() = false

    override fun launchYodleeSplash(attributes: YodleeAttributes, bankId: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.content_frame, YodleeSplashFragment.newInstance(attributes, bankId))
            .addToBackStack(YodleeSplashFragment::class.simpleName)
            .commitAllowingStateLoss()
    }

    override fun launchYodleeWebview(attributes: YodleeAttributes, bankId: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.content_frame, YodleeWebViewFragment.newInstance(attributes, bankId))
            .addToBackStack(YodleeWebViewFragment::class.simpleName)
            .commitAllowingStateLoss()
    }

    override fun launchBankLinking(accountProviderId: String, accountId: String, bankId: String) {
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.content_frame,
                LinkBankFragment.newInstance(accountProviderId = accountProviderId, accountId = accountId, bankId)
            )
            .addToBackStack(LinkBankFragment::class.simpleName)
            .commitAllowingStateLoss()
    }

    override fun launchBankLinkingWithError(errorState: ErrorState) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.content_frame, LinkBankFragment.newInstance(errorState))
            .addToBackStack(LinkBankFragment::class.simpleName)
            .commitAllowingStateLoss()
    }

    override fun retry() {
        onBackPressed()
    }

    override fun bankLinkingFinished() {
        finish()
    }

    override fun bankLinkingCancelled() {
        finish()
    }

    companion object {
        private const val LINK_BANK_TRANSFER_KEY = "LINK_BANK_TRANSFER_KEY"

        fun newInstance(linkBankTransfer: LinkBankTransfer, context: Context): Intent {
            val intent = Intent(context, LinkBankActivity::class.java)
            intent.putExtra(LINK_BANK_TRANSFER_KEY, linkBankTransfer)
            return intent
        }
    }
}