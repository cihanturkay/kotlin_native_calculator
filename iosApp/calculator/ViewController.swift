//
// Copyright 2010-2018 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license
// that can be found in the license/LICENSE.txt file.
//

import UIKit
import arithmeticParser

class ViewController: UIViewController, UITextViewDelegate {

    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        self.input.delegate = self
        inputDidChange()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    @IBOutlet var result: UILabel!
    @IBOutlet var input: UITextView!

    private let parser = PartialParser(composer: Calculator(), partialComposer: PartialRenderer())

    @IBAction func numpadButtonPressed(_ sender: UIButton) {
        let title = sender.currentTitle!
        if title == "" {
            return
        }

        if title == "⌫" {
            if !input.text.isEmpty {
                input.text.removeLast()
            }
        } else {
            input.text.append(title)
        }

        inputDidChange()
    }

    private func inputDidChange() {
        let parsed = parser.parseWithPartial(expression: input.text)
        if let resultValue = parsed.expression {
            result.text = "\(resultValue)"
        } else {
            result.text = ""
        }
    }
}
